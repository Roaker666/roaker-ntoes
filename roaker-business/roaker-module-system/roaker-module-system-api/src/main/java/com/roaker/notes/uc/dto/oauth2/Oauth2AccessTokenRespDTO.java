package com.roaker.notes.uc.dto.oauth2;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * OAuth2.0 访问令牌的信息 Response DTO
 *
 * @author lei.rao
 */
@Data
@Accessors(chain = true)
public class Oauth2AccessTokenRespDTO implements Serializable {

    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;

}
