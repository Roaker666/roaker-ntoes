package com.roaker.notes.uc.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.annotation.Unique;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.core.handler.JsonLongSetTypeHandler;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.DataScopeEnum;
import com.roaker.notes.enums.RoleTypeEnums;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "sys_role_info", autoResultMap = true)
@KeySequence("sys_role_info_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleInfoDO extends BaseDO {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 角色名称
     */
    @TableField
    private String name;
    /**
     * 角色标识
     *
     * 枚举
     */
    @TableField
    private String code;
    /**
     * 角色排序
     */
    @TableField
    private Integer sort;
    /**
     * 角色状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 角色类型
     *
     * 枚举 {@link RoleTypeEnums}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private RoleTypeEnums type;
    /**
     * 备注
     */
    @TableField
    private String remark;

    /**
     * 数据范围
     *
     * 枚举 {@link DataScopeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private DataScopeEnum dataScope;
    /**
     * 数据范围(指定部门数组)
     *
     * 适用于 {@link #dataScope} 的值为 {@link DataScopeEnum#DEPT_CUSTOM} 时
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private Set<Long> dataScopeDeptIds;
}
