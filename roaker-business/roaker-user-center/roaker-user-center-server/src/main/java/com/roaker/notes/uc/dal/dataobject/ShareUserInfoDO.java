package com.roaker.notes.uc.dal.dataobject;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户DO
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "share_user_info", autoResultMap = true)
@KeySequence("share_user_info_seq") //用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareUserInfoDO extends BaseDO {
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 区号
     */
    private String cyCode;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 注册 IP
     */
    private String registerIp;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;
}
