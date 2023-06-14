package com.roaker.notes.uc.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.UserTypeEnum;
import lombok.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "user_role", autoResultMap = true)
@KeySequence("user_role_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    @TableField
    private String uid;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private UserTypeEnum userType;
    @TableField
    private String roleId;
    /**
     * 状态 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
}
