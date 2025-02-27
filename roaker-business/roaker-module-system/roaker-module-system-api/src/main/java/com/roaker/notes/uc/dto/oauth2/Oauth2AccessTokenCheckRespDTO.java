package com.roaker.notes.uc.dto.oauth2;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * OAuth2.0 访问令牌的校验 Response DTO
 *
 * @author lei.rao
 */
@Data
public class Oauth2AccessTokenCheckRespDTO implements Serializable {

    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 租户编号
     */
    private Long tenantId;
    /**
     * 授权范围的数组
     */
    private List<String> scopes;

}
