package com.roaker.notes.uc.api.oauth2.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * OAuth2.0 访问令牌的校验 Response DTO
 *
 * @author Roaker
 */
@Data
public class OAuth2AccessTokenCheckRespDTO implements Serializable {

    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 授权范围的数组
     */
    private List<String> scopes;

}
