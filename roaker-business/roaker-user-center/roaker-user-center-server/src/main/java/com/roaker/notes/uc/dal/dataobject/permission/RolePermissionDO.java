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
@TableName(value = "role_permission_info", autoResultMap = true)
@KeySequence("role_permission_info_seq")
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
    @TableField
    private String roleId;
    @TableField
    private String permissionId;
}
