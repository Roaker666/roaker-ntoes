package com.roaker.notes.uc.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 权限 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class PermissionInfoBaseVO {

    @Schema(description = "权限id", required = true, example = "roaker")
    @NotBlank(message = "权限id不能为空")
    @Size(max = 50, message = "权限id长度不能超过50个字符")
    private String permissinId;
    
    @Schema(description = "权限名称", required = true, example = "roaker")
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50个字符")
    private String name;

    @Schema(description = "权限标识,仅权限类型为按钮时，才需要传递", example = "sys:PermissionInfo:add")
    @Size(max = 100)
    private String permission;

    @Schema(description = "类型,参见 PermissionInfoTypeEnum 枚举类", required = true, example = "1")
    @NotNull(message = "权限类型不能为空")
    private Integer type;

    @Schema(description = "显示顺序不能为空", required = true, example = "1024")
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    @Schema(description = "父权限 ID", required = true, example = "1024")
    @NotNull(message = "父权限 ID 不能为空")
    private String parentId;

    @Schema(description = "路由地址,仅权限类型为权限或者目录时，才需要传", example = "post")
    @Size(max = 200, message = "路由地址不能超过200个字符")
    private String path;

    @Schema(description = "权限图标,仅权限类型为权限或者目录时，才需要传", example = "/PermissionInfo/list")
    private String icon;

    @Schema(description = "组件路径,仅权限类型为权限时，才需要传", example = "system/post/index")
    @Size(max = 200, message = "组件路径不能超过255个字符")
    private String component;

    @Schema(description = "组件名", example = "SystemUser")
    private String componentName;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "是否可见", example = "false")
    private Boolean visible;

    @Schema(description = "是否缓存", example = "false")
    private Boolean keepAlive;

    @Schema(description = "是否总是显示", example = "false")
    private Boolean alwaysShow;

}
