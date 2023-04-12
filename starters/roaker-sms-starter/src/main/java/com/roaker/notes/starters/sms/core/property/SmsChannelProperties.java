package com.roaker.notes.starters.sms.core.property;

import com.roaker.notes.starters.sms.core.enums.SmsChannelEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class SmsChannelProperties {
    /**
     * 渠道编号
     */
    @NotNull(message = "短信渠道 ID 不能为空")
    private Long id;
    /**
     * 短信签名
     */
    @NotEmpty(message = "短信签名不能为空")
    private String signature;
    /**
     * 渠道编码
     *
     * 枚举 {@link SmsChannelEnum}
     */
    @NotEmpty(message = "渠道编码不能为空")
    private Integer code;
    /**
     * 短信 API 的账号
     */
    @NotEmpty(message = "短信 API 的账号不能为空")
    private String apiKey;
    /**
     * 短信 API 的密钥
     */
    @NotEmpty(message = "短信 API 的密钥不能为空")
    private String apiSecret;
    /**
     * 短信发送回调 URL
     */
    private String callbackUrl;
}
