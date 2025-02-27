package com.roaker.notes.pay.service.wallet.impl;

import cn.hutool.core.lang.Assert;
import com.roaker.notes.commons.db.PageParam;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.pay.api.core.order.dto.PayOrderCreateReqDTO;
import com.roaker.notes.pay.api.core.refund.dto.PayRefundCreateReqDTO;
import com.roaker.notes.pay.api.enums.order.PayOrderStatusEnum;
import com.roaker.notes.pay.api.enums.refund.PayRefundStatusEnum;
import com.roaker.notes.pay.api.enums.wallet.PayWalletBizTypeEnum;
import com.roaker.notes.pay.converter.wallet.PayWalletConvert;
import com.roaker.notes.pay.converter.wallet.PayWalletRechargeConvert;
import com.roaker.notes.pay.core.enums.refund.PayRefundStatusRespEnum;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.dal.dataobject.refund.PayRefundDO;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletDO;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import com.roaker.notes.pay.dal.mapper.wallet.PayWalletRechargeMapper;
import com.roaker.notes.pay.service.order.PayOrderService;
import com.roaker.notes.pay.service.refund.PayRefundService;
import com.roaker.notes.pay.service.wallet.PayWalletRechargePackageService;
import com.roaker.notes.pay.service.wallet.PayWalletRechargeService;
import com.roaker.notes.pay.service.wallet.PayWalletService;
import com.roaker.notes.pay.vo.wallet.recharge.AppPayWalletRechargeCreateReqVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static cn.hutool.core.util.ObjectUtil.notEqual;
import static com.roaker.notes.commons.utils.date.LocalDateTimeUtils.addTime;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;
import static com.roaker.notes.pay.api.enums.PayErrorCodeConstants.*;

/**
 * 钱包充值 Service 实现类
 *
 */
@Service
@Slf4j
public class PayWalletRechargeServiceImpl implements PayWalletRechargeService {
    private static final String WALLET_RECHARGE_ORDER_SUBJECT = "钱包余额充值";

    @Resource
    private PayWalletRechargeMapper walletRechargeMapper;
    @Resource
    private PayWalletService payWalletService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private PayRefundService payRefundService;
    @Resource
    private PayWalletRechargePackageService payWalletRechargePackageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletRechargeDO createWalletRecharge(String userId, 
                                                    Integer userType, 
                                                    String userIp,
                                                    Long channelId,
                                                    AppPayWalletRechargeCreateReqVO reqVO) {
        // 1.1 计算充值金额
        int payPrice;
        int bonusPrice = 0;
        if (Objects.nonNull(reqVO.getPackageId())) {
            PayWalletRechargePackageDO rechargePackage = payWalletRechargePackageService.validWalletRechargePackage(reqVO.getPackageId());
            payPrice = rechargePackage.getPayPrice();
            bonusPrice = rechargePackage.getBonusPrice();
        } else {
            payPrice = reqVO.getPayPrice();
        }
        // 1.2 插入充值记录
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        PayWalletRechargeDO recharge = PayWalletRechargeConvert.INSTANCE.convert(wallet.getId(), payPrice, bonusPrice, reqVO.getPackageId());
        walletRechargeMapper.insert(recharge);

        // 2.1 创建支付单
        Long payOrderId = payOrderService.createOrder(new PayOrderCreateReqDTO()
                .setAppId(channelId)
                .setUserIp(userIp)
                .setMerchantOrderId(recharge.getId().toString()) // 业务的订单编号
                .setSubject(WALLET_RECHARGE_ORDER_SUBJECT)
                .setBody("")
                .setPrice(recharge.getPayPrice())
                .setExpireTime(addTime(Duration.ofHours(2L))));
        // 2.2 更新钱包充值记录中支付订单
        walletRechargeMapper.updateById(new PayWalletRechargeDO().setId(recharge.getId()).setPayOrderId(payOrderId));
        recharge.setPayOrderId(payOrderId);
        return recharge;
    }

    @Override
    public PageResult<PayWalletRechargeDO> getWalletRechargePackagePage(String userId, 
                                                                        Integer userType,
                                                                        PageParam pageReqVO, Boolean payStatus) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        return walletRechargeMapper.selectPage(pageReqVO, wallet.getId(), payStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWalletRechargerPaid(Long id, Long payOrderId) {
        // 1.1 获取钱包充值记录
        PayWalletRechargeDO walletRecharge = walletRechargeMapper.selectById(id);
        if (walletRecharge == null) {
            log.error("[updateWalletRechargerPaid][钱包充值记录不存在，钱包充值记录 id({})]", id);
            throw exception(WALLET_RECHARGE_NOT_FOUND);
        }
        // 1.2 校验钱包充值是否可以支付
        PayOrderDO payOrderDO = validateWalletRechargerCanPaid(walletRecharge, payOrderId);

        // 2. 更新钱包充值的支付状态
        int updateCount = walletRechargeMapper.updateByIdAndPaid(id, false,
                new PayWalletRechargeDO().setId(id).setPayStatus(true).setPayTime(LocalDateTime.now())
                        .setPayChannelCode(payOrderDO.getChannelCode()));
        if (updateCount == 0) {
            throw exception(WALLET_RECHARGE_UPDATE_PAID_STATUS_NOT_UNPAID);
        }

        // 3. 更新钱包余额
        // TODO 这样的话，未来提现会不会把充值的，也提现走哈。类似先充 100，送 110；然后提现 110；
        // TODO 需要钱包中加个可提现余额
        payWalletService.addWalletBalance(walletRecharge.getWalletId(), String.valueOf(id),
                PayWalletBizTypeEnum.RECHARGE, walletRecharge.getTotalPrice());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundWalletRecharge(Long id, String userIp, Long channelId, String reason) {
        // 1.1 获取钱包充值记录
        PayWalletRechargeDO walletRecharge = walletRechargeMapper.selectById(id);
        if (walletRecharge == null) {
            log.error("[refundWalletRecharge][钱包充值记录不存在，钱包充值记录 id({})]", id);
            throw exception(WALLET_RECHARGE_NOT_FOUND);
        }
        // 1.2 校验钱包充值是否可以发起退款
        PayWalletDO wallet = validateWalletRechargeCanRefund(walletRecharge);

        // 2. 冻结退款的余额，暂时只处理赠送的余额也全部退回
        payWalletService.freezePrice(wallet.getId(), walletRecharge.getTotalPrice());

        // 3. 创建退款单
        String walletRechargeId = String.valueOf(id);
        String refundId = walletRechargeId + "-refund";
        Long payRefundId = payRefundService.createPayRefund(new PayRefundCreateReqDTO()
                .setAppId(channelId).setUserIp(userIp)
                .setMerchantOrderId(walletRechargeId)
                .setMerchantRefundId(refundId)
                .setReason(reason)
                .setPrice(walletRecharge.getPayPrice()));

        // 4. 更新充值记录退款单号
        // TODO：一般新建这种 update 对象，建议是，第一个 set id 属性，容易知道以它为更新
        walletRechargeMapper.updateById(new PayWalletRechargeDO()
                .setPayRefundId(payRefundId)
                .setRefundStatus(PayRefundStatusEnum.WAITING)
                .setId(walletRecharge.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWalletRechargeRefunded(Long id, Long payRefundId) {
        // 1.1 获取钱包充值记录
        PayWalletRechargeDO walletRecharge = walletRechargeMapper.selectById(id);
        if (walletRecharge == null) {
            log.error("[updateWalletRechargerPaid][钱包充值记录不存在，钱包充值记录 id({})]", id);
            throw exception(WALLET_RECHARGE_NOT_FOUND);
        }
        // 1.2 校验钱包充值是否可以更新已退款
        PayRefundDO payRefund = validateWalletRechargeCanRefunded(walletRecharge, payRefundId);

        PayWalletRechargeDO updateObj = new PayWalletRechargeDO().setId(id);
        // 退款成功
        if (PayRefundStatusEnum.isSuccess(payRefund.getPayRefundStatus().getCode())) {
            // 2.1 更新钱包余额
            payWalletService.reduceWalletBalance(walletRecharge.getWalletId(), id,
                    PayWalletBizTypeEnum.RECHARGE_REFUND, walletRecharge.getTotalPrice());

            updateObj.setRefundStatus(PayRefundStatusEnum.SUCCESS)
                    .setRefundTime(payRefund.getSuccessTime())
                    .setRefundTotalPrice(walletRecharge.getTotalPrice())
                    .setRefundPayPrice(walletRecharge.getPayPrice())
                    .setRefundBonusPrice(walletRecharge.getBonusPrice());
        }
        // 退款失败
        if (PayRefundStatusRespEnum.isFailure(payRefund.getPayRefundStatus().getCode())) {
            // 2.2 解冻余额
            payWalletService.unfreezePrice(walletRecharge.getWalletId(), walletRecharge.getTotalPrice());

            updateObj.setRefundStatus(PayRefundStatusEnum.FAILURE);
        }
        // 3. 更新钱包充值的退款字段
        walletRechargeMapper.updateByIdAndRefunded(id, PayRefundStatusEnum.WAITING.getCode(), updateObj);
    }

    private PayRefundDO validateWalletRechargeCanRefunded(PayWalletRechargeDO walletRecharge, Long payRefundId) {
        // 1. 校验退款订单匹配
        if (notEqual(walletRecharge.getPayRefundId(), payRefundId)) {
            log.error("[validateWalletRechargeCanRefunded][钱包充值({}) 退款单不匹配({})，请进行处理！钱包充值的数据是：{}]",
                    walletRecharge.getId(), payRefundId, JacksonUtils.toJSON(walletRecharge));
            throw exception(WALLET_RECHARGE_REFUND_FAIL_REFUND_ORDER_ID_ERROR);
        }

        // 2.1 校验退款订单
        PayRefundDO payRefund = payRefundService.getRefund(payRefundId);
        if (payRefund == null) {
            log.error("[validateWalletRechargeCanRefunded][payRefund({})不存在]", payRefundId);
            throw exception(WALLET_RECHARGE_REFUND_FAIL_REFUND_NOT_FOUND);
        }
        // 2.2 校验退款金额一致
        if (notEqual(payRefund.getRefundPrice(), walletRecharge.getPayPrice())) {
            log.error("[validateWalletRechargeCanRefunded][钱包({}) payRefund({}) 退款金额不匹配，请进行处理！钱包数据是：{}，payRefund 数据是：{}]",
                    walletRecharge.getId(), payRefundId, JacksonUtils.toJSON(walletRecharge), JacksonUtils.toJSON(payRefund));
            throw exception(WALLET_RECHARGE_REFUND_FAIL_REFUND_PRICE_NOT_MATCH);
        }
        // 2.3 校验退款订单商户订单是否匹配
        if (notEqual(payRefund.getMerchantOrderId(), walletRecharge.getId().toString())) {
            log.error("[validateWalletRechargeCanRefunded][钱包({}) 退款单不匹配({})，请进行处理！payRefund 数据是：{}]",
                    walletRecharge.getId(), payRefundId, JacksonUtils.toJSON(payRefund));
            throw exception(WALLET_RECHARGE_REFUND_FAIL_REFUND_ORDER_ID_ERROR);
        }
        return payRefund;
    }

    private PayWalletDO validateWalletRechargeCanRefund(PayWalletRechargeDO walletRecharge) {
        // 校验充值订单是否支付
        if (!walletRecharge.getPayStatus()) {
            throw exception(WALLET_RECHARGE_REFUND_FAIL_NOT_PAID);
        }
        // 校验充值订单是否已退款
        if (walletRecharge.getPayRefundId() != null) {
            throw exception(WALLET_RECHARGE_REFUND_FAIL_REFUNDED);
        }
        // 校验钱包余额是否足够
        PayWalletDO wallet = payWalletService.getWallet(walletRecharge.getWalletId());
        Assert.notNull(wallet, "用户钱包({}) 不存在", wallet.getId());
        if (wallet.getBalance() < walletRecharge.getTotalPrice()) {
            throw exception(WALLET_RECHARGE_REFUND_BALANCE_NOT_ENOUGH);
        }
        // TODO：需要考虑下，赠送的金额，会不会导致提现超过；
        return wallet;
    }

    private PayOrderDO validateWalletRechargerCanPaid(PayWalletRechargeDO walletRecharge, Long payOrderId) {
        // 1.1 校验充值记录的支付状态
        if (walletRecharge.getPayStatus()) {
            log.error("[validateWalletRechargerCanPaid][钱包({}) 不处于未支付状态!  钱包数据是：{}]",
                    walletRecharge.getId(), JacksonUtils.toJSON(walletRecharge));
            throw exception(WALLET_RECHARGE_UPDATE_PAID_STATUS_NOT_UNPAID);
        }
        // 1.2 校验支付订单匹配
        if (notEqual(walletRecharge.getPayOrderId(), payOrderId)) { // 支付单号
            log.error("[validateWalletRechargerCanPaid][钱包({}) 支付单不匹配({})，请进行处理！ 钱包数据是：{}]",
                    walletRecharge.getId(), payOrderId, JacksonUtils.toJSON(walletRecharge));
            throw exception(WALLET_RECHARGE_UPDATE_PAID_PAY_ORDER_ID_ERROR);
        }

        // 2.1 校验支付单是否存在
        PayOrderDO payOrder = payOrderService.getOrder(payOrderId);
        if (payOrder == null) {
            log.error("[validateWalletRechargerCanPaid][钱包({}) payOrder({}) 不存在，请进行处理！]",
                    walletRecharge.getId(), payOrderId);
            throw exception(PAY_ORDER_NOT_FOUND);
        }
        // 2.2 校验支付单已支付
        if (!PayOrderStatusEnum.isSuccess(payOrder.getPayOrderStatus().getCode())) {
            log.error("[validateWalletRechargerCanPaid][钱包({}) payOrder({}) 未支付，请进行处理！payOrder 数据是：{}]",
                    walletRecharge.getId(), payOrderId, JacksonUtils.toJSON(payOrder));
            throw exception(WALLET_RECHARGE_UPDATE_PAID_PAY_ORDER_STATUS_NOT_SUCCESS);
        }
        // 2.3 校验支付金额一致
        if (notEqual(payOrder.getPrice(), walletRecharge.getPayPrice())) {
            log.error("[validateDemoOrderCanPaid][钱包({}) payOrder({}) 支付金额不匹配，请进行处理！钱包 数据是：{}，payOrder 数据是：{}]",
                    walletRecharge.getId(), payOrderId, JacksonUtils.toJSON(walletRecharge), JacksonUtils.toJSON(payOrder));
            throw exception(WALLET_RECHARGE_UPDATE_PAID_PAY_PRICE_NOT_MATCH);
        }
        // 2.4 校验支付订单的商户订单匹配
        if (notEqual(payOrder.getMerchantOrderId(), walletRecharge.getId().toString())) {
            log.error("[validateDemoOrderCanPaid][钱包({}) 支付单不匹配({})，请进行处理！payOrder 数据是：{}]",
                    walletRecharge.getId(), payOrderId, JacksonUtils.toJSON(payOrder));
            throw exception(WALLET_RECHARGE_UPDATE_PAID_PAY_ORDER_ID_ERROR);
        }
        return payOrder;
    }

}
