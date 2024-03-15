package com.roaker.notes.pay.core.client.impl.mock;

import com.roaker.notes.pay.core.client.dto.order.PayOrderRespDTO;
import com.roaker.notes.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.roaker.notes.pay.core.client.dto.refund.PayRefundRespDTO;
import com.roaker.notes.pay.core.client.dto.refund.PayRefundUnifiedReqDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferRespDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferUnifiedReqDTO;
import com.roaker.notes.pay.core.client.impl.AbstractPayClient;
import com.roaker.notes.pay.core.client.impl.NonePayClientConfig;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.core.enums.transfer.PayTransferTypeEnum;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lei.rao
 * @since 1.0
 */
public class MockPayClient extends AbstractPayClient<NonePayClientConfig> {
    private static final String MOCK_RESP_SUCCESS_DATA = "MOCK_SUCCESS";

    public MockPayClient(Long channelId, NonePayClientConfig config) {
        super(channelId, PayChannelEnum.MOCK.getCode(), config);
    }
    @Override
    protected void doInit() {

    }

    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) throws Throwable {
        return PayOrderRespDTO.successOf("MOCK-P-" + reqDTO.getOutTradeNo(), "", LocalDateTime.now(),
                reqDTO.getOutTradeNo(), MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayOrderRespDTO doParseOrderNotify(Map<String, String> params, String body) throws Throwable {
        throw new UnsupportedOperationException("模拟支付无支付回调");
    }

    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) throws Throwable {
        return PayOrderRespDTO.successOf("MOCK-P-" + outTradeNo, "", LocalDateTime.now(),
                outTradeNo, MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayRefundRespDTO doUnifiedRefund(PayRefundUnifiedReqDTO reqDTO) throws Throwable {
        return PayRefundRespDTO.successOf("MOCK-R-" + reqDTO.getOutRefundNo(), LocalDateTime.now(),
                reqDTO.getOutRefundNo(), MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayRefundRespDTO doParseRefundNotify(Map<String, String> params, String body) throws Throwable {
        throw new UnsupportedOperationException("模拟支付无退款回调");
    }

    @Override
    protected PayRefundRespDTO doGetRefund(String outTradeNo, String outRefundNo) throws Throwable {
        return PayRefundRespDTO.successOf("MOCK-R-" + outRefundNo, LocalDateTime.now(),
                outRefundNo, MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayTransferRespDTO doUnifiedTransfer(PayTransferUnifiedReqDTO reqDTO) throws Throwable {
        throw new UnsupportedOperationException("待实现");
    }

    @Override
    protected PayTransferRespDTO doGetTransfer(String outTradeNo, PayTransferTypeEnum type) throws Throwable {
        throw new UnsupportedOperationException("待实现");
    }
}
