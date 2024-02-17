package com.roaker.notes.uc.dal.dataobject.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import lombok.*;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_roaker_file")
@KeySequence("sys_roaker_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDO extends BaseDO implements Serializable {
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
    private Long configId;

    /**
     * 原文件名
     */
    private String name;

    /**
     * 路径，即文件名
     */
    private String path;

    /**
     * 访问地址
     */
    private String url;
    /**
     * 文件的 MIME 类型，例如 "application/octet-stream"
     */
    private String type;
    /**
     * 文件大小
     */
    private Integer size;
}
