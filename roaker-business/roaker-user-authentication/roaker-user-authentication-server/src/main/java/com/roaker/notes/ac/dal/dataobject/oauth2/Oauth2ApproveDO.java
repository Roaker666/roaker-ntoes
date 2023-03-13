package com.roaker.notes.ac.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "oauth2_approve_tab", autoResultMap = true)
@KeySequence("oauth2_approve_tab_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Oauth2ApproveDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;
    private Long userId;
    private Integer userType;
    private String clientId;
    /**
     * 授权范围
     */
    private String scope;
    /**
     * 是否接受
     * true - 接受
     */
    private Boolean approved;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;
}
