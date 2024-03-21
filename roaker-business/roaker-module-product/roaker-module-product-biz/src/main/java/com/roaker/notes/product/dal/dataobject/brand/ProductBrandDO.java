package com.roaker.notes.product.dal.dataobject.brand;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.ShowStatusEnum;
import lombok.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_product_brand")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrandDO extends BaseDO {
    /**
     * 品牌编号
     */
    @TableId
    @IsNotNull
    @IsAutoIncrement
    private Long id;
    /**
     * 品牌名称
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String name;
    /**
     * 品牌图片
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String logo;
    /**
     * 专区大图
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String bigPic;
    /**
     * 首字母
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.CHAR, length = 4)
    private String firstLetter;
    /**
     * 品牌描述
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 255)
    private String description;
    /**
     * 品牌故事
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.TEXT)
    private String brandStory;
    /**
     * 状态
     * 枚举 {@link ShowStatusEnum}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.INT)
    private ShowStatusEnum showStatus;
    /**
     * 品牌排序
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.INT)
    private Integer sort;
}
