package com.roaker.notes.ac.api.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum Oauth2GrantTypeEnum {
    /**
     * 密码模式
     */
    PASSWORD("password"),
    /**
     * 授权模式
     */
    AUTHORIZATION_CODE("authorization_code"),
    /**
     * 简化模式
     */
    IMPLICIT("implicit"),
    /**
     * 客户端模式
     */
    CLIENT_CREDENTIALS("client_credentials"),
    /**
     * 刷新模式
     */
    REFRESH_TOKEN("refresh_token");
    private final String grantType;

    public static Oauth2GrantTypeEnum getByGranType(String grantType) {
        return ArrayUtil.firstMatch(o -> o.getGrantType().equals(grantType), values());
    }
}
