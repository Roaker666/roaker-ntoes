package com.roaker.notes.uc.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.PermissionTypeEnum;
import lombok.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "sys_permission_info", autoResultMap = true)
@KeySequence("sys_permission_info_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionInfoDO extends BaseDO {

    /**
     * 菜单编号 - 根节点
     */
    public static final Long ID_ROOT = 0L;

    /**
     * 菜单编号
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 菜单名称
     */
    @TableField
    private String name;
    /**
     * 权限标识
     *
     * 一般格式为：${系统}:${模块}:${操作}
     * 例如说：system:admin:add，即 system 服务的添加管理员。
     *
     * 当我们把该 MenuDO 赋予给角色后，意味着该角色有该资源：
     * - 对于后端，配合 @PreAuthorize 注解，配置 API 接口需要该权限，从而对 API 接口进行权限控制。
     * - 对于前端，配合前端标签，配置按钮是否展示，避免用户没有该权限时，结果可以看到该操作。
     */
    @TableField
    private String permission;
    /**
     * 菜单类型
     *
     * 枚举 {@link PermissionTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PermissionTypeEnum type;
    /**
     * 显示顺序
     */
    @TableField
    private Integer sort;
    /**
     * 父菜单ID
     */
    @TableField
    private Long parentId;
    /**
     * 路由地址
     *
     * 如果 path 为 http(s) 时，则它是外链
     */
    @TableField
    private String path;
    /**
     * 菜单图标
     */
    @TableField
    private String icon;
    /**
     * 组件路径
     */
    @TableField
    private String component;
    /**
     * 组件名
     */
    @TableField
    private String componentName;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 是否可见
     *
     * 只有菜单、目录使用
     * 当设置为 true 时，该菜单不会展示在侧边栏，但是路由还是存在。例如说，一些独立的编辑页面 /edit/1024 等等
     */
    @TableField
    private Boolean visible;
    /**
     * 是否缓存
     *
     * 只有菜单、目录使用，否使用 Vue 路由的 keep-alive 特性
     * 注意：如果开启缓存，则必须填写 {@link #componentName} 属性，否则无法缓存
     */
    @TableField
    private Boolean keepAlive;
    /**
     * 是否总是显示
     *
     * 如果为 false 时，当该菜单只有一个子菜单时，不展示自己，直接展示子菜单
     */
    @TableField
    private Boolean alwaysShow;
}
