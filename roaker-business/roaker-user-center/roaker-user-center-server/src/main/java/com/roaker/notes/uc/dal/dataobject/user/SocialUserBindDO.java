package com.roaker.notes.uc.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.SocialTypeEnum;
import com.roaker.notes.enums.UserTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "social_user_bind", autoResultMap = true)
@KeySequence("social_user_bind_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SocialUserBindDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 关联的用户编号
     *
     * 关联 UserDO 的编号
     */
    @TableField
    private String uid;
    /**
     * 用户类型
     *
     * 枚举 {@link UserTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private UserTypeEnum userType;

    /**
     * 社交平台的用户编号
     *
     * 关联 {@link SocialUserDO#getId()}
     */
    @TableField
    private Long socialUserId;
    /**
     * 社交平台的类型
     *
     * 冗余 {@link SocialUserDO#getType()}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private SocialTypeEnum socialType;
}
