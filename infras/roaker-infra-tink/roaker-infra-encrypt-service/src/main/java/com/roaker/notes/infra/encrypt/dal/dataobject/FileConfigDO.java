package com.roaker.notes.infra.encrypt.dal.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.file.core.client.FileClientConfig;
import com.roaker.notes.file.core.enums.FileStorageEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "roaker_file_config", autoResultMap = true)
@KeySequence("roaker_file_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FileConfigDO extends BaseDO implements Serializable {
    /**
     * 配置编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 配置名
     */
    @TableField
    private String name;
    /**
     * 存储器
     *
     * 枚举 {@link com.roaker.notes.file.core.enums.FileStorageEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private FileStorageEnum storage;
    /**
     * 备注
     */
    @TableField
    private String remark;
    /**
     * 是否为主配置
     *
     * 由于我们可以配置多个文件配置，默认情况下，使用主配置进行文件的上传
     */
    @TableField
    @ColumnType(MySqlTypeConstant.BIT)
    private Boolean master;

    /**
     * 支付渠道配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private FileClientConfig config;
}
