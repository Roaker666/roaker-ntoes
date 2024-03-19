package com.roaker.notes.pay.service.tranfer.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.pay.api.core.transfer.dto.PayTransferCreateReqDTO;
import com.roaker.notes.pay.api.enums.notify.PayNotifyTypeEnum;
import com.roaker.notes.pay.converter.transfer.PayTransferConvert;
import com.roaker.notes.pay.core.client.PayClient;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferRespDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferUnifiedReqDTO;
import com.roaker.notes.pay.core.enums.transfer.PayTransferStatusRespEnum;
import com.roaker.notes.pay.core.enums.transfer.PayTransferTypeEnum;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.channel.PayChannelDO;
import com.roaker.notes.pay.dal.dataobject.tranfer.PayTransferDO;
import com.roaker.notes.pay.dal.mapper.tranfer.PayTransferMapper;
import com.roaker.notes.pay.service.app.PayAppService;
import com.roaker.notes.pay.service.channel.PayChannelService;
import com.roaker.notes.pay.service.notify.PayNotifyService;
import com.roaker.notes.pay.service.tranfer.PayTransferService;
import com.roaker.notes.pay.vo.tranfer.PayTransferCreateReqVO;
import com.roaker.notes.pay.vo.tranfer.PayTransferPageReqVO;
import com.roaker.notes.uc.api.seq.SeqApi;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;
import static com.roaker.notes.pay.api.enums.PayErrorCodeConstants.*;
import static com.roaker.notes.pay.core.enums.transfer.PayTransferStatusRespEnum.isInProgress;
import static com.roaker.notes.pay.core.enums.transfer.PayTransferStatusRespEnum.isPendingStatus;
import static com.roaker.notes.pay.core.enums.transfer.PayTransferStatusRespEnum.isSuccess;
import static com.roaker.notes.pay.core.enums.transfer.PayTransferStatusRespEnum.isWaiting;

/**
 * 转账 Service 实现类
 *
 * @author jason
 */
@Service
@Slf4j
public class PayTransferServiceImpl implements PayTransferService {

    private static final String TRANSFER_NO_PREFIX = "T";

    @Resource
    private PayTransferMapper transferMapper;
    @Resource
    private PayAppService appService;
    @Resource
    private PayChannelService channelService;
    @Resource
    private PayNotifyService notifyService;
    @Resource
    private SeqApi seqApi;
    @Resource
    private Validator validator;

    @Override
    public PayTransferDO createTransfer(PayTransferCreateReqVO reqVO, String userIp) {
        // 1. 校验参数
        reqVO.validate(validator);

        // 2. 创建转账单，发起转账
        PayTransferCreateReqDTO req = PayTransferConvert.INSTANCE.convert(reqVO).setUserIp(userIp);
        Long transferId = createTransfer(req);

        // 3. 返回转账单
        return getTransfer(transferId);
    }

    @Override
    public Long createTransfer(PayTransferCreateReqDTO reqDTO) {
        // 1.1 校验 App
        PayAppDO payApp = appService.validPayApp(reqDTO.getAppId());
        // 1.2 校验支付渠道是否有效
        PayChannelDO channel = channelService.validPayChannel(reqDTO.getAppId(), reqDTO.getChannelCode());
        PayClient client = channelService.getPayClient(channel.getId());
        if (client == null) {
            log.error("[createTransfer][渠道编号({}) 找不到对应的支付客户端]", channel.getId());
            throw exception(CHANNEL_NOT_FOUND);
        }
        // 1.3 校验转账单已经发起过转账。
        PayTransferDO transfer = validateTransferCanCreate(reqDTO);

        if (transfer == null) {
            // 2.不存在创建转账单. 否则允许使用相同的 no 再次发起转账
            String no = seqApi.getSegmentId(TRANSFER_NO_PREFIX);
            transfer = PayTransferConvert.INSTANCE.convert(reqDTO)
                    .setChannelId(channel.getId())
                    .setNo(no)
                    .setPayTransferStatus(PayTransferStatusRespEnum.WAITING)
                    .setNotifyUrl(payApp.getTransferNotifyUrl());
            transferMapper.insert(transfer);
        }
        try {
            // 3. 调用三方渠道发起转账
            PayTransferUnifiedReqDTO transferUnifiedReq = PayTransferConvert.INSTANCE.convert2(transfer)
                    .setOutTransferNo(transfer.getNo());
            PayTransferRespDTO unifiedTransferResp = client.unifiedTransfer(transferUnifiedReq);
            // 4. 通知转账结果
            getSelf().notifyTransfer(channel, unifiedTransferResp);
        } catch (Throwable e) {
            // 注意这里仅打印异常，不进行抛出。
            // 原因是：虽然调用支付渠道进行转账发生异常（网络请求超时），实际转账成功。这个结果，后续转账轮询可以拿到。
            // 或者使用相同 no 再次发起转账请求
            log.error("[createTransfer][转账 id({}) requestDTO({}) 发生异常]", transfer.getId(), reqDTO, e);
        }

        return transfer.getId();
    }

    private PayTransferDO validateTransferCanCreate(PayTransferCreateReqDTO dto) {
        PayTransferDO transfer = transferMapper.selectByAppIdAndMerchantTransferId(dto.getAppId(), dto.getMerchantTransferId());
        if (transfer != null) {
            // 已经存在,并且状态不为等待状态。说明已经调用渠道转账并返回结果.
            if (PayTransferStatusRespEnum.WAITING != transfer.getPayTransferStatus()) {
                throw exception(PAY_MERCHANT_TRANSFER_EXISTS);
            }
            if (ObjectUtil.notEqual(dto.getPrice(), transfer.getPrice())) {
                throw exception(PAY_SAME_MERCHANT_TRANSFER_PRICE_NOT_MATCH);
            }
            if (ObjectUtil.notEqual(dto.getType(), transfer.getPayType())) {
                throw exception(PAY_SAME_MERCHANT_TRANSFER_TYPE_NOT_MATCH);
            }
        }
        // 如果状态为等待状态。不知道渠道转账是否发起成功。 允许使用相同的 no 再次发起转账，渠道会保证幂等
        return transfer;
    }

    @Transactional(rollbackFor = Exception.class)
    // 注意，如果是方法内调用该方法，需要通过 getSelf().notifyTransfer(channel, notify) 调用，否则事务不生效
    public void notifyTransfer(PayChannelDO channel, PayTransferRespDTO notify) {
        // 转账成功的回调
        if (isSuccess(notify.getStatus())) {
            notifyTransferSuccess(channel, notify);
        }
        // 转账关闭的回调
        if (PayTransferStatusRespEnum.isClosed(notify.getStatus())) {
            notifyTransferClosed(channel, notify);
        }
        // 转账处理中的回调
        if (isInProgress(notify.getStatus())) {
            notifyTransferInProgress(channel, notify);
        }
        // WAITING 状态无需处理
    }

    private void notifyTransferInProgress(PayChannelDO channel, PayTransferRespDTO notify) {
        // 1.校验
        PayTransferDO transfer = transferMapper.selectByNo(notify.getOutTransferNo());
        if (transfer == null) {
            throw exception(PAY_TRANSFER_NOT_FOUND);
        }
        if (isInProgress(transfer.getPayTransferStatus().getCode())) { // 如果已经是转账中，直接返回，不用重复更新
            return;
        }
        if (!isWaiting(transfer.getPayTransferStatus().getCode())) {
            throw exception(PAY_TRANSFER_STATUS_IS_NOT_WAITING);
        }
        // 2.更新
        int updateCounts = transferMapper.updateByIdAndStatus(transfer.getId(),
                CollUtil.newArrayList(PayTransferStatusRespEnum.WAITING.getCode()),
                new PayTransferDO().setPayTransferStatus(PayTransferStatusRespEnum.IN_PROGRESS));
        if (updateCounts == 0) {
            throw exception(PAY_TRANSFER_STATUS_IS_NOT_WAITING);
        }
        log.info("[notifyTransferInProgress][transfer({}) 更新为转账进行中状态]", transfer.getId());

        // 3. 插入转账通知记录
        notifyService.createPayNotifyTask(PayNotifyTypeEnum.TRANSFER.getCode(), transfer.getId());
    }


    private void notifyTransferSuccess(PayChannelDO channel, PayTransferRespDTO notify) {
        // 1.校验
        PayTransferDO transfer = transferMapper.selectByNo(notify.getOutTransferNo());
        if (transfer == null) {
            throw exception(PAY_TRANSFER_NOT_FOUND);
        }
        if (isSuccess(transfer.getPayTransferStatus().getCode())) { // 如果已成功，直接返回，不用重复更新
            return;
        }
        if (!isPendingStatus(transfer.getPayTransferStatus().getCode())) {
            throw exception(PAY_TRANSFER_STATUS_IS_NOT_PENDING);
        }
        // 2.更新
        int updateCounts = transferMapper.updateByIdAndStatus(transfer.getId(),
                CollUtil.newArrayList(PayTransferStatusRespEnum.WAITING.getCode(), PayTransferStatusRespEnum.IN_PROGRESS.getCode()),
                new PayTransferDO()
                        .setPayTransferStatus(PayTransferStatusRespEnum.SUCCESS)
                        .setSuccessTime(notify.getSuccessTime())
                        .setChannelTransferNo(notify.getChannelTransferNo())
                        .setChannelId(channel.getId())
                        .setChannelCode(channel.getCode())
                        .setChannelNotifyData(JacksonUtils.toJSON(notify)));
        if (updateCounts == 0) {
            throw exception(PAY_TRANSFER_STATUS_IS_NOT_PENDING);
        }
        log.info("[updateTransferSuccess][transfer({}) 更新为已转账]", transfer.getId());

        // 3. 插入转账通知记录
        notifyService.createPayNotifyTask(PayNotifyTypeEnum.TRANSFER.getCode(), transfer.getId());
    }

    private void notifyTransferClosed(PayChannelDO channel, PayTransferRespDTO notify) {
        // 1.校验
        PayTransferDO transfer = transferMapper.selectByNo(notify.getOutTransferNo());
        if (transfer == null) {
            throw exception(PAY_TRANSFER_NOT_FOUND);
        }
        if (PayTransferStatusRespEnum.isClosed(transfer.getPayTransferStatus().getCode())) { // 如果已是关闭状态，直接返回，不用重复更新
            log.info("[updateTransferClosed][transfer({}) 已经是关闭状态，无需更新]", transfer.getId());
            return;
        }
        if (!isPendingStatus(transfer.getPayTransferStatus().getCode())) {
            throw exception(PAY_TRANSFER_STATUS_IS_NOT_PENDING);
        }

        // 2.更新
        int updateCount = transferMapper.updateByIdAndStatus(transfer.getId(),
                CollUtil.newArrayList(PayTransferStatusRespEnum.WAITING.getCode(), PayTransferStatusRespEnum.IN_PROGRESS.getCode()),
                new PayTransferDO()
                        .setPayTransferStatus(PayTransferStatusRespEnum.CLOSED)
                        .setChannelId(channel.getId())
                        .setChannelCode(channel.getCode())
                        .setChannelTransferNo(notify.getChannelTransferNo())
                        .setChannelErrorCode(notify.getChannelErrorCode())
                        .setChannelErrorMsg(notify.getChannelErrorMsg())
                        .setChannelNotifyData(JacksonUtils.toJSON(notify)));
        if (updateCount == 0) {
            throw exception(PAY_TRANSFER_STATUS_IS_NOT_PENDING);
        }
        log.info("[updateTransferClosed][transfer({}) 更新为关闭状态]", transfer.getId());

        // 3. 插入转账通知记录
        notifyService.createPayNotifyTask(PayNotifyTypeEnum.TRANSFER.getCode(), transfer.getId());

    }

    @Override
    public PayTransferDO getTransfer(Long id) {
        return transferMapper.selectById(id);
    }

    @Override
    public PageResult<PayTransferDO> getTransferPage(PayTransferPageReqVO pageReqVO) {
        return transferMapper.selectPage(pageReqVO);
    }

    @Override
    public int syncTransfer() {
        List<PayTransferDO> list = transferMapper.selectListByStatus(PayTransferStatusRespEnum.WAITING.getCode());
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        int count = 0;
        for (PayTransferDO transfer : list) {
            count += syncTransfer(transfer) ? 1 : 0;
        }
        return count;
    }

    private boolean syncTransfer(PayTransferDO transfer) {
        try {
            // 1. 查询转账订单信息
            PayClient payClient = channelService.getPayClient(transfer.getChannelId());
            if (payClient == null) {
                log.error("[syncTransfer][渠道编号({}) 找不到对应的支付客户端]", transfer.getChannelId());
                return false;
            }
            PayTransferRespDTO resp = payClient.getTransfer(transfer.getNo(),transfer.getPayType());
            // 2. 回调转账结果
            notifyTransfer(transfer.getChannelId(), resp);
            return true;
        } catch (Throwable ex) {
            log.error("[syncTransfer][transfer({}) 同步转账单状态异常]", transfer.getId(), ex);
            return false;
        }
    }

    private void notifyTransfer(Long channelId, PayTransferRespDTO notify) {
        // 校验渠道是否有效
        PayChannelDO channel = channelService.validPayChannel(channelId);
        // 通知转账结果给对应的业务
        getSelf().notifyTransfer(channel, notify);
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private PayTransferServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
