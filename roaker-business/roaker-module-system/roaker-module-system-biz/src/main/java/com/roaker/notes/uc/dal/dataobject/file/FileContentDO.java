package com.roaker.notes.uc.dal.dataobject.file;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.file.core.client.db.DBFileClient;
import lombok.*;

import java.io.Serializable;

/**
 * 文件内容表
 * 专门用于存储 {@link DBFileClient} 的文件内容
 *
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_roaker_file_content")
@KeySequence("sys_roaker_file_content_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileContentDO extends BaseDO implements Serializable {
    /**
     * 编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 配置编号
     *
     * 关联 {@link FileConfigDO#getId()}
     */
    @TableField
    private Long configId;
    /**
     * 路径，即文件名
     */
    @TableField
    private String path;
    /**
     * 文件内容
     */
    @TableField
    @ColumnType(MySqlTypeConstant.BLOB)
    private byte[] content;
}
