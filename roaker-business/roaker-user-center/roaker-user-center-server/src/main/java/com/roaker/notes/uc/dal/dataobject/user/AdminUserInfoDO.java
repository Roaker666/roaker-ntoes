package com.roaker.notes.uc.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.annotation.BizKey;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户DO
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "admin_user_info", autoResultMap = true)
@KeySequence("admin_user_info_seq") //用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BizKey(bizName = "admin_user_info", bizPrefix = "AUI")
public class AdminUserInfoDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String uid;
    /**
     * 用户昵称
     */
    @TableField
    private String username;
    /**
     * 头像
     */
    @TableField
    private String avatar;
    /**
     * 区号
     */
    @TableField
    private String cyCode;
    /**
     * 手机
     */
    @TableField
    private String mobile;
    /**
     * 邮箱
     */
    @TableField
    private String email;
    /**
     * 状态 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;

    /**
     * 注册 IP
     */
    @TableField
    private String registerIp;
    /**
     * 最后登录IP
     */
    @TableField
    private String loginIp;
    /**
     * 最后登录时间
     */
    @TableField
    private LocalDateTime loginDate;
}
