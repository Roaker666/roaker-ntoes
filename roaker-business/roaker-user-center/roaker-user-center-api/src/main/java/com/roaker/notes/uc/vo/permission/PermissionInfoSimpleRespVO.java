package com.roaker.notes.uc.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 菜单精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionInfoSimpleRespVO {

    @Schema(description = "菜单编号", required = true, example = "1024")
    private String permissionId;

    @Schema(description = "菜单名称", required = true, example = "芋道")
    private String permissionName;

    @Schema(description = "父菜单 ID", required = true, example = "1024")
    private String parentId;

    @Schema(description = "类型,参见 PermissionInfoTypeEnum 枚举类", required = true, example = "1")
    private Integer type;

}
