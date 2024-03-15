package com.roaker.notes.uc.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Schema(description = "管理后台 - 菜单更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionInfoUpdateReqVO extends PermissionInfoBaseVO {

    @Schema(description = "菜单编号", required = true, example = "1024")
    @NotNull(message = "菜单编号不能为空")
    private String permissionId;

}
