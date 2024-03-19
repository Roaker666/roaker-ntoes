package com.roaker.notes.uc.dal.dataobject.dict;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_dynamic_dict_config")
@KeySequence("sys_dynamic_dict_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
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
     * 配置类型{@link DynamicDictTypeDO#getDictType()} ()}
     */
    @TableField
    private String type;
    /**
     * 配置业务名称
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String bizName;
    /**
     * 配置所属类型key
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String bizKey;
    /**
     * 模板枚举配置全类目
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
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
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String name;
    /**
     * 上游业务码
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String label;
    /**
     * 上游业务描述名称
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String value;
    /**
     * 状态
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 颜色类型
     * 对应到 element-ui 为 default、primary、success、info、warning、danger
     */
    @TableField
    private String colorType;
    /**
     * css 样式
     */
    @TableField
    private String cssClass;
    /**
     * 字典排序
     */
    @TableField
    private Integer sort;
    /**
     * 备注
     */
    @TableField
    private String remark;
}
