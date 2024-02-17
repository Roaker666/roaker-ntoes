package com.roaker.notes.uc.api.sms.dto.code;

import com.roaker.notes.commons.validation.InEnum;
import com.roaker.notes.commons.validation.Mobile;
import com.roaker.notes.uc.enums.notify.SmsSceneEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 短信验证码的发送 Request DTO
 *
 * @author Roaker
 */
@Data
public class SmsCodeSendReqDTO {

    /**
     * 手机号
     */
    @Mobile
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    /**
     * 发送场景
     */
    @NotNull(message = "发送场景不能为空")
    private Integer scene;
    /**
     * 发送 IP
     */
    @NotEmpty(message = "发送 IP 不能为空")
    private String createIp;

}
