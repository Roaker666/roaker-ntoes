package com.roaker.notes.ac.dal.dataobject.credentials;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
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
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 认证类型，如手机号+密码、邮箱+密码等
     */
    private Integer identityType;
    /**
     * 标示，如手机号、用户ID等
     */
    private String identifier;
    /**
     * 认证凭证，如密码/pin等，当一些特殊的可以以json格式存储
     */
    private String credentials;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 状态
     */
    private Integer status;
}
