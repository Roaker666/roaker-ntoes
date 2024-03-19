package com.roaker.notes.pay.core.client.impl.weixin;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.pay.core.client.dto.order.PayOrderRespDTO;
import com.roaker.notes.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.core.enums.order.PayOrderDisplayModeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信支付【App 支付】的 PayClient 实现类
 * 文档：<a href="https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter2_5_3.shtml">App 支付</a>
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class WxAppPayClient extends AbstractWxPayClient{
    public WxAppPayClient(Long channelId, WxPayClientConfig config) {
        super(channelId, PayChannelEnum.WX_APP.getChannelCode(), config);
    }

    @Override
    protected void doInit() {
        super.doInit(WxPayConstants.TradeType.APP);
    }


    @Override
    protected PayOrderRespDTO doUnifiedOrderV2(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderRequest 对象
        WxPayUnifiedOrderRequest request = buildPayUnifiedOrderRequestV2(reqDTO);
        // 执行请求
        WxPayMpOrderResult response = client.createOrder(request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.APP.getName(), JacksonUtils.toJSON(response),
                reqDTO.getOutTradeNo(), response);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrderV3(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderV3Request 对象
        WxPayUnifiedOrderV3Request request = buildPayUnifiedOrderRequestV3(reqDTO);
        // 执行请求
        WxPayUnifiedOrderV3Result.AppResult response = client.createOrderV3(TradeTypeEnum.APP, request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.APP.getName(), JacksonUtils.toJSON(response),
                reqDTO.getOutTradeNo(), response);
    }
}
