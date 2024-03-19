package com.roaker.notes.pay.service.notify.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.commons.utils.date.DateUtils;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.lock.RoakerLockUtils;
import com.roaker.notes.pay.api.core.notify.dto.PayOrderNotifyReqDTO;
import com.roaker.notes.pay.api.core.notify.dto.PayRefundNotifyReqDTO;
import com.roaker.notes.pay.api.core.notify.dto.PayTransferNotifyReqDTO;
import com.roaker.notes.pay.api.enums.notify.PayNotifyStatusEnum;
import com.roaker.notes.pay.api.enums.notify.PayNotifyTypeEnum;
import com.roaker.notes.pay.dal.dataobject.notify.PayNotifyLogDO;
import com.roaker.notes.pay.dal.dataobject.notify.PayNotifyTaskDO;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.dal.dataobject.refund.PayRefundDO;
import com.roaker.notes.pay.dal.dataobject.tranfer.PayTransferDO;
import com.roaker.notes.pay.dal.mapper.notify.PayNotifyLogMapper;
import com.roaker.notes.pay.dal.mapper.notify.PayNotifyTaskMapper;
import com.roaker.notes.pay.framework.KeyConstants;
import com.roaker.notes.pay.service.notify.PayNotifyService;
import com.roaker.notes.pay.service.order.PayOrderService;
import com.roaker.notes.pay.service.refund.PayRefundService;
import com.roaker.notes.pay.service.tranfer.PayTransferService;
import com.roaker.notes.pay.vo.notify.PayNotifyTaskPageReqVO;
import com.roaker.notes.vo.CommonResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.roaker.notes.commons.utils.date.LocalDateTimeUtils.addTime;
import static com.roaker.notes.pay.framework.job.config.PayJobConfiguration.NOTIFY_THREAD_POOL_TASK_EXECUTOR;

/**
 * 支付通知 Core Service 实现类
 */
@Service
@Valid
@Slf4j
public class PayNotifyServiceImpl implements PayNotifyService {
    /**
     * 通知超时时间，单位：秒
     */
    public static final int NOTIFY_TIMEOUT = 120;
    /**
     * {@link #NOTIFY_TIMEOUT} 的毫秒
     */
    public static final long NOTIFY_TIMEOUT_MILLIS = 120 * DateUtils.SECOND_MILLIS;

    @Resource
    @Lazy // 循环依赖，避免报错
    private PayOrderService orderService;
    @Resource
    @Lazy // 循环依赖，避免报错
    private PayRefundService refundService;
    @Resource
    @Lazy // 循环依赖，避免报错
    private PayTransferService transferService;

    @Resource
    private PayNotifyTaskMapper notifyTaskMapper;
    @Resource
    private PayNotifyLogMapper notifyLogMapper;

    @Resource(name = NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private RoakerLockUtils roakerLockUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPayNotifyTask(Integer type, Long dataId) {
        PayNotifyTaskDO task = new PayNotifyTaskDO().
                setPayType(CommonEnum.of(type, PayNotifyTypeEnum.class))
                .setDataId(dataId)
                .setPayNotifyStatus(PayNotifyStatusEnum.WAITING)
                .setNextNotifyTime(LocalDateTime.now())
                .setNotifyTimes(0)
                .setMaxNotifyTimes(PayNotifyTaskDO.NOTIFY_FREQUENCY.length + 1);
        // 补充 appId + notifyUrl 字段
        if (Objects.equals(task.getPayType(), PayNotifyTypeEnum.ORDER)) {
            PayOrderDO order = orderService.getOrder(task.getDataId()); // 不进行非空判断，有问题直接异常
            task.setAppId(order.getAppId())
                    .setMerchantOrderId(order.getMerchantOrderId())
                    .setNotifyUrl(order.getNotifyUrl());
        } else if (Objects.equals(task.getPayType(), PayNotifyTypeEnum.REFUND)) {
            PayRefundDO refundDO = refundService.getRefund(task.getDataId());
            task.setAppId(refundDO.getAppId())
                    .setMerchantOrderId(refundDO.getMerchantOrderId())
                    .setNotifyUrl(refundDO.getNotifyUrl());
        } else if (Objects.equals(task.getPayType(), PayNotifyTypeEnum.TRANSFER)) {
            PayTransferDO transfer = transferService.getTransfer(task.getDataId());
            task.setAppId(transfer.getAppId())
                    .setMerchantTransferId(transfer.getMerchantTransferId())
                    .setNotifyUrl(transfer.getNotifyUrl());
        }

        // 执行插入
        notifyTaskMapper.insert(task);

        // 必须在事务提交后，在发起任务，否则 PayNotifyTaskDO 还没入库，就提前回调接入的业务
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                executeNotify(task);
            }
        });
    }

    @Override
    public int executeNotify() throws InterruptedException {
        // 获得需要通知的任务
        List<PayNotifyTaskDO> tasks = notifyTaskMapper.selectListByNotify();
        if (CollUtil.isEmpty(tasks)) {
            return 0;
        }

        // 遍历，逐个通知
        CountDownLatch latch = new CountDownLatch(tasks.size());
        tasks.forEach(task -> threadPoolTaskExecutor.execute(() -> {
            try {
                executeNotify(task);
            } finally {
                latch.countDown();
            }
        }));
        // 等待完成
        awaitExecuteNotify(latch);
        // 返回执行完成的任务数（成功 + 失败)
        return tasks.size();
    }

    /**
     * 等待全部支付通知的完成
     * 每 1 秒会打印一次剩余任务数量
     *
     * @param latch Latch
     * @throws InterruptedException 如果被打断
     */
    private void awaitExecuteNotify(CountDownLatch latch) throws InterruptedException {
        long size = latch.getCount();
        for (int i = 0; i < NOTIFY_TIMEOUT; i++) {
            if (latch.await(1L, TimeUnit.SECONDS)) {
                return;
            }
            log.info("[awaitExecuteNotify][任务处理中， 总任务数({}) 剩余任务数({})]", size, latch.getCount());
        }
        log.error("[awaitExecuteNotify][任务未处理完，总任务数({}) 剩余任务数({})]", size, latch.getCount());
    }

    /**
     * 同步执行单个支付通知
     *
     * @param task 通知任务
     */
    public void executeNotify(PayNotifyTaskDO task) {
        // 分布式锁，避免并发问题
        roakerLockUtils.runWithLock(KeyConstants.formatNotifyTask(task.getId()), () -> {
            // 校验，当前任务是否已经被通知过
            // 虽然已经通过分布式加锁，但是可能同时满足通知的条件，然后都去获得锁。此时，第一个执行完后，第二个还是能拿到锁，然后会再执行一次。
            // 因此，此处我们通过第 notifyTimes 通知次数是否匹配来判断
            PayNotifyTaskDO dbTask = notifyTaskMapper.selectById(task.getId());
            if (ObjectUtil.notEqual(task.getNotifyTimes(), dbTask.getNotifyTimes())) {
                log.warn("[executeNotifySync][task({}) 任务被忽略，原因是它的通知不是第 ({}) 次，可能是因为并发执行了]",
                        JacksonUtils.toJSON(task), dbTask.getNotifyTimes());
                return;
            }
            // 执行通知
            getSelf().executeNotify0(dbTask);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void executeNotify0(PayNotifyTaskDO task) {
        // 发起回调
        CommonResult<?> invokeResult = null;
        Throwable invokeException = null;
        try {
            invokeResult = executeNotifyInvoke(task);
        } catch (Throwable e) {
            invokeException = e;
        }
        // 处理结果
        Integer newStatus = processNotifyResult(task, invokeResult, invokeException);
        // 记录 PayNotifyLog 日志
        String response = invokeException != null ? ExceptionUtil.getRootCauseMessage(invokeException) :
                JacksonUtils.toJSON(invokeResult);
        notifyLogMapper.insert(PayNotifyLogDO.builder()
                .taskId(task.getId())
                .notifyTimes(task.getNotifyTimes() + 1)
                .payStatus(CommonEnum.of(newStatus, PayNotifyStatusEnum.class))
                .response(response)
                .build());
    }

    /**
     * 执行单个支付任务的 HTTP 调用
     *
     * @param task 通知任务
     * @return HTTP 响应
     */
    private CommonResult<?> executeNotifyInvoke(PayNotifyTaskDO task) {
        // 拼接 body 参数
        Object request;
        if (Objects.equals(task.getPayType(), PayNotifyTypeEnum.ORDER)) {
            request = PayOrderNotifyReqDTO.builder()
                    .merchantOrderId(task.getMerchantOrderId())
                    .payOrderId(task.getDataId())
                    .build();
        } else if (Objects.equals(task.getPayType(), PayNotifyTypeEnum.REFUND)) {
            request = PayRefundNotifyReqDTO.builder()
                    .merchantOrderId(task.getMerchantOrderId())
                    .payRefundId(task.getDataId())
                    .build();
        } else if (Objects.equals(task.getPayType(), PayNotifyTypeEnum.TRANSFER)) {
            request = new PayTransferNotifyReqDTO()
                    .setMerchantTransferId(task.getMerchantTransferId())
                    .setPayTransferId(task.getDataId());
        } else {
            throw new RuntimeException("未知的通知任务类型：" + JacksonUtils.toJSON(task));
        }
        // 拼接 header 参数
        Map<String, String> headers = new HashMap<>();
        // 发起请求
        try (HttpResponse response = HttpUtil.createPost(task.getNotifyUrl())
                .body(JacksonUtils.toJSON(request))
                .addHeaders(headers)
                .timeout((int) NOTIFY_TIMEOUT_MILLIS)
                .execute()) {
            // 解析结果
            return JacksonUtils.from(response.body(), CommonResult.class);
        }
    }

    /**
     * 处理并更新通知结果
     *
     * @param task            通知任务
     * @param invokeResult    通知结果
     * @param invokeException 通知异常
     * @return 最终任务的状态
     */
    @VisibleForTesting
    Integer processNotifyResult(PayNotifyTaskDO task, CommonResult<?> invokeResult, Throwable invokeException) {
        // 设置通用的更新 PayNotifyTaskDO 的字段
        PayNotifyTaskDO updateTask = new PayNotifyTaskDO()
                .setId(task.getId())
                .setLastExecuteTime(LocalDateTime.now())
                .setNotifyTimes(task.getNotifyTimes() + 1);

        // 情况一：调用成功
        if (invokeResult != null && invokeResult.isSuccess()) {
            updateTask.setPayNotifyStatus(PayNotifyStatusEnum.SUCCESS);
            notifyTaskMapper.updateById(updateTask);
            return updateTask.getPayNotifyStatus().getCode();
        }

        // 情况二：调用失败、调用异常
        // 2.1 超过最大回调次数
        if (updateTask.getNotifyTimes() >= PayNotifyTaskDO.NOTIFY_FREQUENCY.length) {
            updateTask.setPayNotifyStatus(PayNotifyStatusEnum.FAILURE);
            notifyTaskMapper.updateById(updateTask);
            return updateTask.getPayNotifyStatus().getCode();
        }
        // 2.2 未超过最大回调次数
        updateTask.setNextNotifyTime(addTime(Duration.ofSeconds(PayNotifyTaskDO.NOTIFY_FREQUENCY[updateTask.getNotifyTimes()])));
        updateTask.setPayNotifyStatus(invokeException != null ? PayNotifyStatusEnum.REQUEST_FAILURE : PayNotifyStatusEnum.REQUEST_SUCCESS);
        notifyTaskMapper.updateById(updateTask);
        return updateTask.getPayNotifyStatus().getCode();
    }

    @Override
    public PayNotifyTaskDO getNotifyTask(Long id) {
        return notifyTaskMapper.selectById(id);
    }

    @Override
    public PageResult<PayNotifyTaskDO> getNotifyTaskPage(PayNotifyTaskPageReqVO pageReqVO) {
        return notifyTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PayNotifyLogDO> getNotifyLogList(Long taskId) {
        return notifyLogMapper.selectListByTaskId(taskId);
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private PayNotifyServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

}
