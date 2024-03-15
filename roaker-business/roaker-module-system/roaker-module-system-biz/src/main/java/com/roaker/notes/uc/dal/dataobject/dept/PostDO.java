package com.roaker.notes.uc.dal.dataobject.dept;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;

/**
 * 岗位表
 *
 * @author ruoyi
 */
@TableName("sys_post")
@KeySequence("sys_post_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDO extends BaseDO {

    /**
     * 岗位序号
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 岗位名称
     */
    @TableField
    private String name;
    /**
     * 岗位编码
     */
    @TableField
    private String code;
    /**
     * 岗位排序
     */
    @TableField
    private Integer sort;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 备注
     */
    @TableField
    private String remark;

}
