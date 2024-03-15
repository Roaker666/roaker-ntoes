package com.roaker.notes.uc.api.social.dto;

import com.roaker.notes.commons.validation.InEnum;
import com.roaker.notes.enums.SocialTypeEnum;
import com.roaker.notes.enums.UserTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 取消绑定社交用户 Request DTO
 *
 * @author Roaker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserBindReqDTO {

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
     * 社交平台的类型
     */
    @NotNull(message = "社交平台的类型不能为空")
    private Integer socialType;
    /**
     * 授权码
     */
    @NotEmpty(message = "授权码不能为空")
    private String code;
    /**
     * state
     */
    @NotNull(message = "state 不能为空")
    private String state;

}
