package com.roaker.notes.pay.core.enums.channel;

import cn.hutool.core.util.ArrayUtil;
import com.roaker.notes.pay.core.client.PayClientConfig;
import com.roaker.notes.pay.core.client.impl.NonePayClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    WX_PUB("wx_pub", "微信 JSAPI 支付", WxPayClientConfig.class), // 公众号网页
    WX_LITE("wx_lite", "微信小程序支付", WxPayClientConfig.class),
    WX_APP("wx_app", "微信 App 支付", WxPayClientConfig.class),
    WX_NATIVE("wx_native", "微信 Native 支付", WxPayClientConfig.class),
    WX_WAP("wx_wap", "微信 Wap 网站支付", WxPayClientConfig.class), // H5 网页
    WX_BAR("wx_bar", "微信付款码支付", WxPayClientConfig.class),

    ALIPAY_PC("alipay_pc", "支付宝 PC 网站支付", AlipayPayClientConfig.class),
    ALIPAY_WAP("alipay_wap", "支付宝 Wap 网站支付", AlipayPayClientConfig.class),
    ALIPAY_APP("alipay_app", "支付宝App 支付", AlipayPayClientConfig.class),
    ALIPAY_QR("alipay_qr", "支付宝扫码支付", AlipayPayClientConfig.class),
    ALIPAY_BAR("alipay_bar", "支付宝条码支付", AlipayPayClientConfig.class),
    MOCK("mock", "模拟支付", NonePayClientConfig.class),

    WALLET("wallet", "钱包支付", NonePayClientConfig.class);

    private final String code;
    private final String name;
    private final Class<? extends PayClientConfig> configClass;

    /**
     * 微信支付
     */
    public static final String WECHAT = "WECHAT";

    /**
     * 支付宝支付
     */
    public static final String ALIPAY = "ALIPAY";


    public static PayChannelEnum getByCode(String code) {
        return ArrayUtil.firstMatch(o -> o.getCode().equals(code), values());
    }

    public static boolean isAlipay(String channelCode) {
        return channelCode != null && channelCode.startsWith("alipay");
    }
}
