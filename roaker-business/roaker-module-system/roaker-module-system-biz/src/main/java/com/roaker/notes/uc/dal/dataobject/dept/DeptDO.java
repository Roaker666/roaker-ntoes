package com.roaker.notes.uc.dal.dataobject.dept;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import lombok.*;

/**
 * 部门表
 *
 * @author ruoyi
 * @author Roaker
 */
@TableName("sys_dept")
@KeySequence("sys_dept_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeptDO extends BaseDO {

    /**
     * 部门ID
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 部门名称
     */
    @TableField
    private String name;
    /**
     * 父部门ID
     *
     * 关联 {@link #id}
     */
    @TableField
    private Long parentId;
    /**
     * 显示顺序
     */
    @TableField
    private Integer sort;
    /**
     * 负责人
     *
     * 关联 {@link AdminUserInfoDO#getUid()} ()}
     */
    @TableField
    private String leaderUserId;
    /**
     * 联系电话
     */
    @TableField
    private String phone;
    /**
     * 邮箱
     */
    @TableField
    private String email;
    /**
     * 部门状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;

}
