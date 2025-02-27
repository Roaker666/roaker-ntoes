package com.roaker.notes.uc.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 菜单列表 Request VO")
@Data
public class PermissionInfoListReqVO {

    @Schema(description = "菜单名称,模糊匹配", example = "Roaker")
    private String name;

    @Schema(description = "展示状态,参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

}
