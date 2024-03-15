package com.roaker.notes.pay.core.client.impl.alipay;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.roaker.notes.commons.utils.ObjectUtils;
import com.roaker.notes.pay.core.client.dto.order.PayOrderRespDTO;
import com.roaker.notes.pay.core.client.dto.refund.PayRefundRespDTO;
import com.roaker.notes.pay.core.client.dto.refund.PayRefundUnifiedReqDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferRespDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferUnifiedReqDTO;
import com.roaker.notes.pay.core.client.impl.AbstractPayClient;
import com.roaker.notes.pay.core.enums.order.PayOrderStatusRespEnum;
import com.roaker.notes.pay.core.enums.transfer.PayTransferTypeEnum;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.roaker.notes.pay.core.client.impl.alipay.AlipayPayClientConfig.MODE_CERTIFICATE;

/**
 * 支付宝抽象类，实现支付宝统一的接口、以及部分实现（退款）
 *
 * @author lei.rao
 * @since 1.0
 */
public abstract class AbstractAlipayPayClient extends AbstractPayClient<AlipayPayClientConfig> {
    protected DefaultAlipayClient client;

    public AbstractAlipayPayClient(Long channelId, String channelCode, AlipayPayClientConfig config) {
        super(channelId, channelCode, config);
    }

    @Override
    @SneakyThrows
    protected void doInit() {
        AlipayConfig alipayConfig = new AlipayConfig();
        BeanUtil.copyProperties(config, alipayConfig, false);
        this.client = new DefaultAlipayClient(alipayConfig);
    }


    // ============ 支付相关 ==========

    @Override
    protected PayOrderRespDTO doParseOrderNotify(Map<String, String> params, String body) throws Throwable {
        //1、校验回调数据
        Map<String, String> bodyObj = HttpUtil.decodeParamMap(body, StandardCharsets.UTF_8);
        AlipaySignature.rsaCheckV2(bodyObj, config.getAlipayPublicKey(), StandardCharsets.UTF_8.name(), config.getSignType());
        //2、解析订单状态
        //额外说明：支付宝不仅仅支付成功会回调，再各种触发支付单数据变化时，都会进行回调，所以这里 status 的解析会写的比较复杂
        Integer status = parseStatus(bodyObj.get("trande_status"));
        //特殊逻辑: 支付宝没有退款成功的状态，所以，如果有退款金额，我们认为是退款成功
        if (MapUtil.getDouble(bodyObj, "refund_fee", 0D) > 0) {
            status = PayOrderStatusRespEnum.REFUND.getCode();
        }
        Assert.notNull(status, (Supplier<Throwable>) () -> {
            throw new IllegalArgumentException(StrUtil.format("body({}) 的 trade_status 不正确", body));
        });
        return PayOrderRespDTO.of(status, bodyObj.get("trade_no"), bodyObj.get("seller_id"), parseTime(params.get("gmt_payment")),
                bodyObj.get("out_trade_no"), body);
    }

    private static Integer parseStatus(String tradeStatus) {
        return Objects.equals("WAIT_BUYER_PAY", tradeStatus) ? PayOrderStatusRespEnum.WAITING.getCode()
                : ObjectUtils.equalsAny(tradeStatus, "TRADE_FINISHED", "TRADE_SUCCESS") ? PayOrderStatusRespEnum.SUCCESS.getCode()
                : Objects.equals("TRADE_CLOSED", tradeStatus) ? PayOrderStatusRespEnum.CLOSED.getCode() : null;
    }

    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) throws Throwable {
        // 1.1 构建 AlipayTradeRefundModel 请求
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        // 1.2 构建 AlipayTradeQueryRequest 请求
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        AlipayTradeQueryResponse response;
        if (Objects.equals(config.getMode(), MODE_CERTIFICATE)) {
            // 证书模式
            response = client.certificateExecute(request);
        } else {
            response = client.execute(request);
        }
        if (!response.isSuccess()) { // 不成功，例如说订单不存在
            return PayOrderRespDTO.closedOf(response.getSubCode(), response.getSubMsg(),
                    outTradeNo, response);
        }
        // 2.2 解析订单的状态
        Integer status = parseStatus(response.getTradeStatus());
        Assert.notNull(status, () -> {
            throw new IllegalArgumentException(StrUtil.format("body({}) 的 trade_status 不正确", response.getBody()));
        });
        return PayOrderRespDTO.of(status, response.getTradeNo(), response.getBuyerUserId(), LocalDateTimeUtil.of(response.getSendPayDate()),
                outTradeNo, response);
    }

    // ============ 退款相关 ==========
    /**
     * 支付宝统一的退款接口 alipay.trade.refund
     *
     * @param reqDTO 退款请求 request DTO
     * @return 退款请求 Response
     */
    @Override
    protected PayRefundRespDTO doUnifiedRefund(PayRefundUnifiedReqDTO reqDTO) throws Throwable {
        // 1.1 构建 AlipayTradeRefundModel 请求
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(reqDTO.getOutTradeNo());
        model.setOutRequestNo(reqDTO.getOutRefundNo());
        model.setRefundAmount(formatAmount(reqDTO.getRefundPrice()));
        model.setRefundReason(reqDTO.getReason());
        return null;
    }

    @Override
    protected PayRefundRespDTO doParseRefundNotify(Map<String, String> params, String body) throws Throwable {
        return null;
    }

    @Override
    protected PayRefundRespDTO doGetRefund(String outTradeNo, String outRefundNo) throws Throwable {
        return null;
    }

    @Override
    protected PayTransferRespDTO doUnifiedTransfer(PayTransferUnifiedReqDTO reqDTO) throws Throwable {
        return null;
    }

    @Override
    protected PayTransferRespDTO doGetTransfer(String outTradeNo, PayTransferTypeEnum type) throws Throwable {
        return null;
    }

    protected String formatAmount(Integer amount) {
        return String.valueOf(amount / 100.0);
    }
}
