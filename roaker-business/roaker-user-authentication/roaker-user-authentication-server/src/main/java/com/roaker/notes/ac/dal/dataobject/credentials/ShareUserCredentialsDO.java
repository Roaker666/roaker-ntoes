package com.roaker.notes.ac.dal.dataobject.credentials;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.ac.api.enums.CredentialsIdentifyTypeEnums;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "share_user_credentials", autoResultMap = true)
@KeySequence("share_user_credentials_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareUserCredentialsDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 用户ID
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.BIGINT)
    private Long uid;
    /**
     * 认证类型，如手机号+密码、邮箱+密码等
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CredentialsIdentifyTypeEnums identityType;
    /**
     * 标示，如手机号、用户ID等
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR)
    private String identifier;
    /**
     * 认证凭证，如密码/pin等，当一些特殊的可以以json格式存储
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 512)
    private String credentials;
    /**
     * 过期时间
     */
    @TableField
    private LocalDateTime expireTime;

    /**
     * 状态
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
}
