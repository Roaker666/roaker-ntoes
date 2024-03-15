package com.roaker.notes.uc.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Schema(description = "管理后台 - 赋予角色菜单 Request VO")
@Data
public class PermissionAssignRolePermissionInfoReqVO {

    @Schema(description = "角色编号", required = true, example = "1")
    @NotNull(message = "角色编号不能为空")
    private Long roleId;

    @Schema(description = "菜单编号列表", example = "1,3,5")
    private Set<Long> permissionInfoIds = Collections.emptySet(); // 兜底

}
