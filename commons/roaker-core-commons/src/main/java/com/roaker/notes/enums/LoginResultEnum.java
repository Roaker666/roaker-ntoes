package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginResultEnum implements CommonEnum {

    SUCCESS(0, "SUCCESS"), // 成功
    BAD_CREDENTIALS(10, "BAD_CREDENTIALS"), // 账号或密码不正确
    USER_DISABLED(20, "USER_DISABLED"), // 用户被禁用
    CAPTCHA_NOT_FOUND(30, "CAPTCHA_NOT_FOUND"), // 图片验证码不存在
    CAPTCHA_CODE_ERROR(31, "CAPTCHA_CODE_ERROR"), // 图片验证码不正确

    ;

    /**
     * 结果
     */
    @EnumValue
    private final Integer code;
    private final String name;

}
