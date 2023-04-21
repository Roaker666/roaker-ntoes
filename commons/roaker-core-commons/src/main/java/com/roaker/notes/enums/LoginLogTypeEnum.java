package com.roaker.notes.enums;

import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginLogTypeEnum implements CommonEnum {

    LOGIN_USERNAME(100, "使用账号登录"),
    LOGIN_SOCIAL(101, "使用社交登录"),
    LOGIN_MOBILE(103, "使用手机登陆"),
    LOGIN_SMS(104, "使用短信登陆"),
    LOGOUT_SELF(200, "自己主动登出"),
    LOGOUT_DELETE(202, "强制退出");

    /**
     * 日志类型
     */
    private final Integer code;

    private final String name;
}