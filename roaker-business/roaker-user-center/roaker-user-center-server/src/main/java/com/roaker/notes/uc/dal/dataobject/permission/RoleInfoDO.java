package com.roaker.notes.uc.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.annotation.Unique;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "role_info", autoResultMap = true)
@KeySequence("role_info_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleInfoDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    @TableField
    @Unique
    private String roleId;
    @TableField
    private String roleName;
    @TableField
    private Integer roleLevel;
    @TableField
    private String parentRoleId;
    /**
     * 分行
     */
    @TableField
    private String branch;
    @TableField
    private Integer sort;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    @TableField
    private String remark;
}
