package com.roaker.notes.uc.dal.dataobject.codegen;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.uc.dal.dataobject.db.DataSourceConfigDO;
import com.roaker.notes.uc.enums.CodegenFrontTypeEnum;
import com.roaker.notes.uc.enums.CodegenSceneEnum;
import com.roaker.notes.uc.enums.CodegenTemplateTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 代码生成 table 表定义
 *
 */
@TableName(value = "sys_codegen_table", autoResultMap = true)
@KeySequence("sys_codegen_table_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodegenTableDO extends BaseDO {

    /**
     * ID 编号
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;

    /**
     * 数据源编号
     *
     * 关联 {@link DataSourceConfigDO#getId()}
     */
    @TableField
    private Long dataSourceConfigId;
    /**
     * 生成场景
     *
     * 枚举 {@link CodegenSceneEnum}
     */
    @TableField
    private Integer scene;

    // ========== 表相关字段 ==========

    /**
     * 表名称
     *
     * 关联 {@link TableInfo#getName()}
     */
    @TableField
    private String tableName;
    /**
     * 表描述
     *
     * 关联 {@link TableInfo#getComment()}
     */
    @TableField
    private String tableComment;
    /**
     * 备注
     */
    @TableField
    private String remark;

    // ========== 类相关字段 ==========

    /**
     * 模块名，即一级目录
     *
     * 例如说，system、infra、tool 等等
     */
    @TableField
    private String moduleName;
    /**
     * 业务名，即二级目录
     *
     * 例如说，user、permission、dict 等等
     */
    @TableField
    private String businessName;
    /**
     * 类名称（首字母大写）
     *
     * 例如说，SysUser、SysMenu、SysDictData 等等
     */
    @TableField
    private String className;
    /**
     * 类描述
     */
    @TableField
    private String classComment;
    /**
     * 作者
     */
    @TableField
    private String author;

    // ========== 生成相关字段 ==========

    /**
     * 模板类型
     *
     * 枚举 {@link CodegenTemplateTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CodegenTemplateTypeEnum templateType;
    /**
     * 代码生成的前端类型
     *
     * 枚举 {@link CodegenFrontTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CodegenFrontTypeEnum frontType;

    // ========== 菜单相关字段 ==========

    /**
     * 父菜单编号
     *
     * 关联 MenuDO 的 id 属性
     */
    @TableField
    private Long parentMenuId;


    // ========== 主子表相关字段 ==========

    /**
     * 主表的编号
     *
     * 关联 {@link CodegenTableDO#getId()}
     */
    @TableField
    private Long masterTableId;
    /**
     * 【自己】子表关联主表的字段编号
     *
     * 关联 {@link CodegenColumnDO#getId()}
     */
    @TableField
    private Long subJoinColumnId;
    /**
     * 主表与子表是否一对多
     *
     * true：一对多
     * false：一对一
     */
    @TableField
    private Boolean subJoinMany;

    // ========== 树表相关字段 ==========

    /**
     * 树表的父字段编号
     *
     * 关联 {@link CodegenColumnDO#getId()}
     */
    @TableField
    private Long treeParentColumnId;
    /**
     * 树表的名字字段编号
     *
     * 名字的用途：新增或修改时，select 框展示的字段
     *
     * 关联 {@link CodegenColumnDO#getId()}
     */
    @TableField
    private Long treeNameColumnId;


}
