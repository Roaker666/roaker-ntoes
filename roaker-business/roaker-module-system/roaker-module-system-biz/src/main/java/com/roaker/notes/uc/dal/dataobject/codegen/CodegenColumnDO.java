package com.roaker.notes.uc.dal.dataobject.codegen;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CodegenColumnHtmlTypeEnum;
import com.roaker.notes.enums.CodegenColumnListConditionEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 代码生成 column 字段定义
 *
 * @author Roaker
 */
@TableName(value = "sys_codegen_column", autoResultMap = true)
@KeySequence("sys_codegen_column_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodegenColumnDO extends BaseDO {

    /**
     * ID 编号
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 表编号
     * <p>
     * 关联 {@link CodegenTableDO#getId()}
     */
    @TableField
    private Long tableId;

    // ========== 表相关字段 ==========

    /**
     * 字段名
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField#getName()}
     */
    @TableField
    private String columnName;
    /**
     * 数据库字段类型
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField.MetaInfo#getJdbcType()}
     */
    @TableField
    private String dataType;
    /**
     * 字段描述
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField#getComment()}
     */
    @TableField
    private String columnComment;
    /**
     * 是否允许为空
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField.MetaInfo#isNullable()}
     */
    @TableField
    private Boolean nullable;
    /**
     * 是否主键
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField#isKeyFlag()}
     */
    @TableField
    private Boolean primaryKey;
    /**
     * 是否自增
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField#isKeyIdentityFlag()}
     */
    @TableField
    private Boolean autoIncrement;
    /**
     * 排序
     */
    @TableField
    private Integer ordinalPosition;

    // ========== Java 相关字段 ==========

    /**
     * Java 属性类型
     * <p>
     * 例如说 String、Boolean 等等
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField#getColumnType()}
     */
    @TableField
    private String javaType;
    /**
     * Java 属性名
     * <p>
     * 关联 {@link com.baomidou.mybatisplus.generator.config.po.TableField#getPropertyName()}
     */
    @TableField
    private String javaField;
    /**
     * 字典类型
     * <p>
     * 关联 DictTypeDO 的 type 属性
     */
    @TableField
    private String dictType;
    /**
     * 数据示例，主要用于生成 Swagger 注解的 example 字段
     */
    @TableField
    private String example;

    // ========== CRUD 相关字段 ==========

    /**
     * 是否为 Create 创建操作的字段
     */
    @TableField
    private Boolean createOperation;
    /**
     * 是否为 Update 更新操作的字段
     */
    @TableField
    private Boolean updateOperation;
    /**
     * 是否为 List 查询操作的字段
     */
    @TableField
    private Boolean listOperation;
    /**
     * List 查询操作的条件类型
     * <p>
     * 枚举 {@link CodegenColumnListConditionEnum}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.INT)
    private CodegenColumnListConditionEnum listOperationCondition;
    /**
     * 是否为 List 查询操作的返回字段
     */
    @TableField
    private Boolean listOperationResult;

    // ========== UI 相关字段 ==========

    /**
     * 显示类型
     * <p>
     * 枚举 {@link CodegenColumnHtmlTypeEnum}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.INT)
    private CodegenColumnHtmlTypeEnum htmlType;

}
