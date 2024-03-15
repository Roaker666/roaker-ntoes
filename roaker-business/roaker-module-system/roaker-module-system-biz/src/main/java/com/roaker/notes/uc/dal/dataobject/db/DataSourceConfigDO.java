package com.roaker.notes.uc.dal.dataobject.db;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.core.handler.EncryptTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_data_source_config", autoResultMap = true)
@KeySequence("sys_data_source_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@Accessors(chain = true)
public class DataSourceConfigDO extends BaseDO {

    /**
     * 主键编号 - Master 数据源
     */
    public static final Long ID_MASTER = 0L;

    /**
     * 主键编号
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 连接名
     */
    @TableField
    private String name;

    /**
     * 数据源连接
     */
    @TableField
    private String url;
    /**
     * 用户名
     */
    @TableField
    private String username;
    /**
     * 密码
     */
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String password;

}