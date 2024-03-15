package com.roaker.notes.uc.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import lombok.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "sys_role_permission_info", autoResultMap = true)
@KeySequence("sys_role_permission_info_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 角色ID
     */
    @TableField
    private Long roleId;
    /**
     * 菜单ID
     */
    @TableField
    private Long menuId;

}
