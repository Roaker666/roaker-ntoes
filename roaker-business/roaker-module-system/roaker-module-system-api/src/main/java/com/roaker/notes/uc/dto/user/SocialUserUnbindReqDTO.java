package com.roaker.notes.uc.dto.user;

import com.roaker.notes.commons.validation.InEnum;
import com.roaker.notes.enums.SocialTypeEnum;
import com.roaker.notes.enums.UserTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 社交绑定 Request DTO，使用 code 授权码
 *
 * @author Roaker
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserUnbindReqDTO {

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
     * 社交平台的 openid
     */
    @NotEmpty(message = "社交平台的 openid 不能为空")
    private String openid;

}
