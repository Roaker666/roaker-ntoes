package com.roaker.notes.uc.dal.dataobject.logger;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.LoginLogTypeEnum;
import com.roaker.notes.enums.LoginResultEnum;
import com.roaker.notes.enums.UserTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 登录日志表
 *
 * 注意，包括登录和登出两种行为
 *
 * @author Roaker源码
 */
@TableName("sys_login_log")
@KeySequence("sys_login_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginLogDO extends BaseDO {

    /**
     * 日志主键
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 日志类型
     *
     * 枚举 {@link LoginLogTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private LoginLogTypeEnum logType;
    /**
     * 链路追踪编号
     */
    @TableField
    private String traceId;
    /**
     * 用户编号
     */
    @TableField
    private String userId;
    /**
     * 用户类型
     *
     * 枚举 {@link UserTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private UserTypeEnum userType;
    /**
     * 用户账号
     *
     * 冗余，因为账号可以变更
     */
    @TableField
    private String username;
    /**
     * 登录结果
     *
     * 枚举 {@link LoginResultEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private LoginResultEnum result;
    /**
     * 用户 IP
     */
    @TableField
    private String userIp;
    /**
     * 浏览器 UA
     */
    @TableField
    private String userAgent;

}
