package com.roaker.notes.uc.dal.dataobject.dict;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_dynamic_dict_typ")
@KeySequence("sys_dynamic_dict_typ_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicDictTypeDO extends BaseDO {
    /**
     * 字典主键
     */
    @TableId
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 字典名称
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String name;
    /**
     * 字典类型
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String dictType;
    /**
     * 状态
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 备注
     */
    @TableField
    private String remark;

    /**
     * 删除时间
     */
    @TableField
    private LocalDateTime deletedTime;
}
