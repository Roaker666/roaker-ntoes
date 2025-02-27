package com.roaker.notes.pay.framework.pay.core;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.roaker.notes.exception.ServiceException;
import com.roaker.notes.pay.api.enums.order.PayOrderStatusEnum;
import com.roaker.notes.pay.api.enums.refund.PayRefundStatusEnum;
import com.roaker.notes.pay.api.enums.wallet.PayWalletBizTypeEnum;
import com.roaker.notes.pay.core.client.dto.order.PayOrderRespDTO;
import com.roaker.notes.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.roaker.notes.pay.core.client.dto.refund.PayRefundRespDTO;
import com.roaker.notes.pay.core.client.dto.refund.PayRefundUnifiedReqDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferRespDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferUnifiedReqDTO;
import com.roaker.notes.pay.core.client.impl.AbstractPayClient;
import com.roaker.notes.pay.core.client.impl.NonePayClientConfig;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.core.enums.refund.PayRefundStatusRespEnum;
import com.roaker.notes.pay.core.enums.transfer.PayTransferTypeEnum;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderExtensionDO;
import com.roaker.notes.pay.dal.dataobject.refund.PayRefundDO;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.roaker.notes.pay.service.order.PayOrderService;
import com.roaker.notes.pay.service.refund.PayRefundService;
import com.roaker.notes.pay.service.wallet.PayWalletService;
import com.roaker.notes.pay.service.wallet.PayWalletTransactionService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.roaker.notes.exception.enums.GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR;
import static com.roaker.notes.pay.api.enums.PayErrorCodeConstants.PAY_ORDER_EXTENSION_NOT_FOUND;
import static com.roaker.notes.pay.api.enums.PayErrorCodeConstants.REFUND_NOT_FOUND;

/**
 * 钱包支付的 PayClient 实现类
 *
 */
@Slf4j
public class WalletPayClient extends AbstractPayClient<NonePayClientConfig> {

    public static final String USER_ID_KEY = "user_id";
    public static final String USER_TYPE_KEY = "user_type";

    private PayWalletService wallService;
    private PayWalletTransactionService walletTransactionService;
    private PayOrderService orderService;
    private PayRefundService refundService;

    public WalletPayClient(Long channelId,  NonePayClientConfig config) {
        super(channelId, PayChannelEnum.WALLET.getChannelCode(), config);
    }

    @Override
    protected void doInit() {
        if (wallService == null) {
            wallService = SpringUtil.getBean(PayWalletService.class);
        }
        if (walletTransactionService == null) {
            walletTransactionService = SpringUtil.getBean(PayWalletTransactionService.class);
        }
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) {
        try {
            String userId = MapUtil.getStr(reqDTO.getChannelExtras(), USER_ID_KEY);
            Integer userType = MapUtil.getInt(reqDTO.getChannelExtras(), USER_TYPE_KEY);
            Assert.notNull(userId, "用户 id 不能为空");
            Assert.notNull(userType, "用户类型不能为空");
            PayWalletTransactionDO transaction = wallService.orderPay(userId, userType, reqDTO.getOutTradeNo(),
                    reqDTO.getPrice());
            return PayOrderRespDTO.successOf(transaction.getNo(), transaction.getCreator(),
                    transaction.getCreateTime(),
                    reqDTO.getOutTradeNo(), transaction);
        } catch (Throwable ex) {
            log.error("[doUnifiedOrder] 失败", ex);
            Integer errorCode = INTERNAL_SERVER_ERROR.getCode();
            String errorMsg = INTERNAL_SERVER_ERROR.getMsg();
            if (ex instanceof ServiceException) {
                ServiceException serviceException = (ServiceException) ex;
                errorCode = serviceException.getCode();
                errorMsg = serviceException.getMessage();
            }
            return PayOrderRespDTO.closedOf(String.valueOf(errorCode), errorMsg,
                    reqDTO.getOutTradeNo(), "");
        }
    }

    @Override
    protected PayOrderRespDTO doParseOrderNotify(Map<String, String> params, String body) {
        throw new UnsupportedOperationException("钱包支付无支付回调");
    }

    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) {
        if (orderService == null) {
            orderService = SpringUtil.getBean(PayOrderService.class);
        }
        PayOrderExtensionDO orderExtension = orderService.getOrderExtensionByNo(outTradeNo);
        // 支付交易拓展单不存在， 返回关闭状态
        if (orderExtension == null) {
            return PayOrderRespDTO.closedOf(String.valueOf(PAY_ORDER_EXTENSION_NOT_FOUND.getCode()),
                    PAY_ORDER_EXTENSION_NOT_FOUND.getMsg(), outTradeNo, "");
        }
        // 关闭状态
        if (PayOrderStatusEnum.CLOSED == orderExtension.getPayStatus()) {
            return PayOrderRespDTO.closedOf(orderExtension.getChannelErrorCode(),
                    orderExtension.getChannelErrorMsg(), outTradeNo, "");
        }
        // 成功状态
        if (PayOrderStatusEnum.SUCCESS == orderExtension.getPayStatus()) {
            PayWalletTransactionDO walletTransaction = walletTransactionService.getWalletTransaction(
                    String.valueOf(orderExtension.getOrderId()), PayWalletBizTypeEnum.PAYMENT);
            Assert.notNull(walletTransaction, "支付单 {} 钱包流水不能为空", outTradeNo);
            return PayOrderRespDTO.successOf(walletTransaction.getNo(), walletTransaction.getCreator(),
                    walletTransaction.getCreateTime(), outTradeNo, walletTransaction);
        }
        // 其它状态为无效状态
        log.error("[doGetOrder] 支付单 {} 的状态不正确", outTradeNo);
        throw new IllegalStateException(String.format("支付单[%s] 状态不正确", outTradeNo));
    }

    @Override
    protected PayRefundRespDTO doUnifiedRefund(PayRefundUnifiedReqDTO reqDTO) {
        try {
            PayWalletTransactionDO payWalletTransaction = wallService.orderRefund(reqDTO.getOutRefundNo(),
                    reqDTO.getRefundPrice(), reqDTO.getReason());
            return PayRefundRespDTO.successOf(payWalletTransaction.getNo(), payWalletTransaction.getCreateTime(),
                    reqDTO.getOutRefundNo(), payWalletTransaction);
        } catch (Throwable ex) {
            log.error("[doUnifiedRefund] 失败", ex);
            Integer errorCode = INTERNAL_SERVER_ERROR.getCode();
            String errorMsg = INTERNAL_SERVER_ERROR.getMsg();
            if (ex instanceof ServiceException) {
                ServiceException serviceException = (ServiceException) ex;
                errorCode =  serviceException.getCode();
                errorMsg = serviceException.getMessage();
            }
            return PayRefundRespDTO.failureOf(String.valueOf(errorCode), errorMsg,
                    reqDTO.getOutRefundNo(), "");
        }
    }

    @Override
    protected PayRefundRespDTO doParseRefundNotify(Map<String, String> params, String body) {
        throw new UnsupportedOperationException("钱包支付无退款回调");
    }

    @Override
    protected PayRefundRespDTO doGetRefund(String outTradeNo, String outRefundNo) {
        if (refundService == null) {
            refundService = SpringUtil.getBean(PayRefundService.class);
        }
        PayRefundDO payRefund = refundService.getRefundByNo(outRefundNo);
        // 支付退款单不存在， 返回退款失败状态
        if (payRefund == null) {
            return PayRefundRespDTO.failureOf(String.valueOf(REFUND_NOT_FOUND), REFUND_NOT_FOUND.getMsg(),
                    outRefundNo, "");
        }
        // 退款失败
        if (PayRefundStatusEnum.FAILURE == payRefund.getPayRefundStatus()) {
            return PayRefundRespDTO.failureOf(payRefund.getChannelErrorCode(), payRefund.getChannelErrorMsg(),
                    outRefundNo, "");
        }
        // 退款成功
        if (PayRefundStatusEnum.SUCCESS == payRefund.getPayRefundStatus()) {
            PayWalletTransactionDO walletTransaction = walletTransactionService.getWalletTransaction(
                    String.valueOf(payRefund.getId()), PayWalletBizTypeEnum.PAYMENT_REFUND);
            Assert.notNull(walletTransaction, "支付退款单 {} 钱包流水不能为空", outRefundNo);
            return PayRefundRespDTO.successOf(walletTransaction.getNo(), walletTransaction.getCreateTime(),
                    outRefundNo, walletTransaction);
        }
        // 其它状态为无效状态
        log.error("[doGetRefund] 支付退款单 {} 的状态不正确", outRefundNo);
        throw new IllegalStateException(String.format("支付退款单[%s] 状态不正确", outRefundNo));
    }

    @Override
    public PayTransferRespDTO doUnifiedTransfer(PayTransferUnifiedReqDTO reqDTO) {
        throw new UnsupportedOperationException("待实现");
    }

    @Override
    protected PayTransferRespDTO doGetTransfer(String outTradeNo, PayTransferTypeEnum type) {
        throw new UnsupportedOperationException("待实现");
    }

}
