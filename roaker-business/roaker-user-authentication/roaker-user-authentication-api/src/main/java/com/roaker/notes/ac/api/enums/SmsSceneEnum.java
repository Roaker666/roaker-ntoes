package com.roaker.notes.ac.api.enums;

import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@Getter
@AllArgsConstructor
public enum SmsSceneEnum implements CommonEnum, IntArrayValuable {
    MEMBER_LOGIN(1, "user-sms-login", "会员用户 - 手机号登陆"),
    MEMBER_UPDATE_MOBILE(2, "user-sms-reset-password", "会员用户 - 修改手机"),
    MEMBER_FORGET_PASSWORD(3, "user-sms-update-mobile", "会员用户 - 忘记密码"),

    ADMIN_MEMBER_LOGIN(21, "admin-sms-login", "后台用户 - 手机号登录");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SmsSceneEnum::getCode).toArray();
    private final Integer code;
    private final String templateCode;
    private final String name;

    @Override
    public int[] array() {
        return new int[0];
    }
}
