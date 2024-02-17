package com.roaker.notes.uc.dal.dataobject.social;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.TenantBaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.SocialTypeEnum;
import com.roaker.notes.enums.UserTypeEnum;
import com.xingyuv.jushauth.config.AuthConfig;
import lombok.*;

/**
 * 社交客户端 DO
 *
 * 对应 {@link AuthConfig} 配置，满足不同租户，有自己的客户端配置，实现社交（三方）登录
 *
 * @author Roaker
 */
@TableName(value = "sys_social_client", autoResultMap = true)
@KeySequence("sys_social_client_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialClientDO extends TenantBaseDO {

    /**
     * 编号，自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 应用名
     */
    @TableField
    private String name;
    /**
     * 社交类型
     *
     * 枚举 {@link SocialTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private SocialTypeEnum socialType;
    /**
     * 用户类型
     *
     * 目的：不同用户类型，对应不同的小程序，需要自己的配置
     *
     * 枚举 {@link UserTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private UserTypeEnum userType;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;

    /**
     * 客户端 id
     */
    @TableField
    private String clientId;
    /**
     * 客户端 Secret
     */
    @TableField
    private String clientSecret;

    /**
     * 代理编号
     *
     * 目前只有部分“社交类型”在使用：
     * 1. 企业微信：对应授权方的网页应用 ID
     */
    @TableField
    private String agentId;

}
