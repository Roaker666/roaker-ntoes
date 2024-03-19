package com.roaker.notes.pay.core.client.impl.alipay;

import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.roaker.notes.pay.core.client.dto.order.PayOrderRespDTO;
import com.roaker.notes.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.core.enums.order.PayOrderDisplayModeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝【App 支付】的 PayClient 实现类
 * 文档：<a href="https://opendocs.alipay.com/open/02e7gq">App 支付</a>
 *
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class AlipayAppPayClient extends AbstractAlipayPayClient {
    public AlipayAppPayClient(Long channelId, AlipayPayClientConfig config) {
        super(channelId, PayChannelEnum.ALIPAY_APP.getChannelCode(), config);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) throws Throwable {
        // 1.1 构建 AlipayTradeAppPayModel 请求
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        // ① 通用的参数
        model.setOutTradeNo(reqDTO.getOutTradeNo());
        model.setSubject(reqDTO.getSubject());
        model.setBody(reqDTO.getBody());
        model.setTotalAmount(formatAmount(reqDTO.getPrice()));
        model.setTimeExpire(formatTime(reqDTO.getExpireTime()));
        model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码：无线快捷支付产品
        // ② 个性化的参数【无】
        // ③ 支付宝扫码支付只有一种展示
        String displayMode = PayOrderDisplayModeEnum.APP.getName();
        // 1.2 构建 AlipayTradePrecreateRequest 请求
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(reqDTO.getNotifyUrl());
        request.setReturnUrl(reqDTO.getReturnUrl());
        // 2.1 执行请求
        AlipayTradeAppPayResponse response = client.sdkExecute(request);
        // 2.2 处理结果
        if (!response.isSuccess()) {
            return buildClosedPayOrderRespDTO(reqDTO, response);
        }

        return PayOrderRespDTO.waitingOf(displayMode, response.getBody(),
                reqDTO.getOutTradeNo(), response);
    }
}
