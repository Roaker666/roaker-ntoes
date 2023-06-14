package com.roaker.notes.uc.bo.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 角色创建 Request BO
 *
 * @author 芋道源码
 */
@Data
public class RoleCreateReqBO {
    /**
     * 角色id
     */
    @NotBlank(message = "角色编号不能为空")
    private String roleId;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /**
     * 角色标志
     */
    @NotBlank(message = "角色标志不能为空")
    @Size(max = 100, message = "角色标志长度不能超过100个字符")
    private String code;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;


}
