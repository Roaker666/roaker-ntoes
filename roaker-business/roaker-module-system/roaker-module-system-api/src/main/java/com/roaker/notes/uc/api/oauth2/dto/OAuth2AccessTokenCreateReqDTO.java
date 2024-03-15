package com.roaker.notes.uc.api.oauth2.dto;

import com.roaker.notes.commons.validation.InEnum;
import com.roaker.notes.enums.UserTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * OAuth2.0 访问令牌创建 Request DTO
 *
 * @author Roaker
 */
@Data
public class OAuth2AccessTokenCreateReqDTO implements Serializable {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private String userId;
    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    private Integer userType;
    /**
     * 客户端编号
     */
    @NotNull(message = "客户端编号不能为空")
    private String clientId;
    /**
     * 授权范围
     */
    private List<String> scopes;

}
