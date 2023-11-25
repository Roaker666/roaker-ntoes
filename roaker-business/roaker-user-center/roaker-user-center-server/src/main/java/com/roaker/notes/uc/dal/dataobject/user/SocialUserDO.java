package com.roaker.notes.uc.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.SocialTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "social_user", autoResultMap = true)
@KeySequence("social_user_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SocialUserDO extends BaseDO {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 社交平台的类型
     * <p>
     * 枚举 {@link SocialTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private SocialTypeEnum type;

    /**
     * 社交 openid
     */
    @TableField
    private String openid;
    /**
     * 社交 token
     */
    @TableField
    private String token;
    /**
     * 原始 Token 数据，一般是 JSON 格式
     */
    @TableField
    @ColumnType(MySqlTypeConstant.JSON)
    private String rawTokenInfo;

    /**
     * 用户昵称
     */
    @TableField
    private String nickname;
    /**
     * 用户头像
     */
    @TableField
    private String avatar;
    /**
     * 原始用户数据，一般是 JSON 格式
     */
    @TableField
    @ColumnType(MySqlTypeConstant.JSON)
    private String rawUserInfo;

    /**
     * 最后一次的认证 code
     */
    @TableField
    private String code;
    /**
     * 最后一次的认证 state
     */
    @TableField
    private String state;
}
