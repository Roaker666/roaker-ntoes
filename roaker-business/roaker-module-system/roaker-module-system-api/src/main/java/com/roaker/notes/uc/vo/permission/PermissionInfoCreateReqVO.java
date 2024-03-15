package com.roaker.notes.uc.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 菜单创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionInfoCreateReqVO extends PermissionInfoBaseVO {
}
