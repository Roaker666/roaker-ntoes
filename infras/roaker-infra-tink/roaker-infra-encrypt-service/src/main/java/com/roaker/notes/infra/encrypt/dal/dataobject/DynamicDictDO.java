package com.roaker.notes.infra.encrypt.dal.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import lombok.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName("roaker_file")
@KeySequence("roaker_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicDictDO extends BaseDO {
    /**
     * 配置编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 配置类型;{@link DynamicDictTypeEnums}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private DynamicDictTypeEnums type;
    /**
     * 配置业务名称
     */
    @TableField
    private String bizName;
    /**
     * 配置所属类型key
     */
    @TableField
    private String bizKey;
    /**
     * 模板枚举配置全类目
     */
    @TableField
    private String bizClass;
    /**
     * 业务码
     */
    @TableField
    private Integer code;
    /**
     * 业务描述名称
     */
    @TableField
    private String name;
    /**
     * 上游业务码
     */
    @TableField
    private String originCode;
    /**
     * 上游业务描述名称
     */
    @TableField
    private String originName;
}
