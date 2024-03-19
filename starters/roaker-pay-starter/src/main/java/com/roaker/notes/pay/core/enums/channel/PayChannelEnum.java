package com.roaker.notes.pay.core.enums.channel;

import cn.hutool.core.util.ArrayUtil;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.pay.core.client.PayClientConfig;
import com.roaker.notes.pay.core.client.impl.NonePayClientConfig;
import com.roaker.notes.pay.core.client.impl.alipay.AlipayPayClientConfig;
import com.roaker.notes.pay.core.client.impl.weixin.WxPayClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum implements CommonEnum {
    WX_PUB(10, "wx_pub", "微信 JSAPI 支付", WxPayClientConfig.class), // 公众号网页
    WX_LITE(20, "wx_lite", "微信小程序支付", WxPayClientConfig.class),
    WX_APP(30, "wx_app", "微信 App 支付", WxPayClientConfig.class),
    WX_NATIVE(40, "wx_native", "微信 Native 支付", WxPayClientConfig.class),
    WX_WAP(50, "wx_wap", "微信 Wap 网站支付", WxPayClientConfig.class), // H5 网页
    WX_BAR(60, "wx_bar", "微信付款码支付", WxPayClientConfig.class),

    ALIPAY_PC(70, "alipay_pc", "支付宝 PC 网站支付", AlipayPayClientConfig.class),
    ALIPAY_WAP(80, "alipay_wap", "支付宝 Wap 网站支付", AlipayPayClientConfig.class),
    ALIPAY_APP(90, "alipay_app", "支付宝App 支付", AlipayPayClientConfig.class),
    ALIPAY_QR(100, "alipay_qr", "支付宝扫码支付", AlipayPayClientConfig.class),
    ALIPAY_BAR(110, "alipay_bar", "支付宝条码支付", AlipayPayClientConfig.class),
    MOCK(120, "mock", "模拟支付", NonePayClientConfig.class),

    WALLET(130, "wallet", "钱包支付", NonePayClientConfig.class);

    private final Integer code;
    private final String channelCode;
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
        return ArrayUtil.firstMatch(o -> o.getChannelCode().equals(code), values());
    }

    public static boolean isAlipay(String channelCode) {
        return channelCode != null && channelCode.startsWith("alipay");
    }
}
