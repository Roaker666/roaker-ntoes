package com.roaker.notes.uc.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.UserTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "sys_oauth2_code_tab", autoResultMap = true)
@KeySequence("sys_oauth2_code_tab_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Oauth2CodeDO extends BaseDO {
    /**
     * 编号，数据库递增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 授权码
     */
    @TableField
    private String code;
    /**
     * 用户编号
     */
    @TableField
    private String userId;
    /**
     * 用户类型
     *
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private UserTypeEnum userType;
    /**
     * 客户端编号
     *
     */
    @TableField
    private String clientId;
    /**
     * 授权范围
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private List<String> scopes;
    /**
     * 重定向地址
     */
    @TableField
    private String redirectUri;
    /**
     * 状态
     */
    @TableField
    private String state;
    /**
     * 过期时间
     */
    @TableField
    private LocalDateTime expiresTime;

}