package com.roaker.notes.pay.core.enums.order;

import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayOrderDisplayModeEnum implements CommonEnum {
    URL(10, "url"), // Redirect 跳转链接的方式
    IFRAME(20, "iframe"), // IFrame 内嵌链接的方式【目前暂时用不到】
    FORM(30, "form"), // HTML 表单提交
    QR_CODE(40, "qr_code"), // 二维码的文字内容
    QR_CODE_URL(50, "qr_code_url"), // 二维码的图片链接
    BAR_CODE(60, "bar_code"), // 条形码
    APP(70, "app"), // 应用：Android、iOS、微信小程序、微信公众号等，需要做自定义处理的
    ;

    /**
     * 展示模式
     */
    private final Integer code;
    private final String name;
}
