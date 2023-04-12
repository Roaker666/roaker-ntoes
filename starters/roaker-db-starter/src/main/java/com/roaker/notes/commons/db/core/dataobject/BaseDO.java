package com.roaker.notes.commons.db.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public abstract class BaseDO implements Serializable {
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建者
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creator;
    /**
     * 更新者
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String modifier;

    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.INTEGER)
    private Integer version;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField
    private Boolean deleted;

    public void setDefault() {
        this.setCreator("sys");
        this.setCreateTime(LocalDateTime.now());
        this.setModifier("sys");
        this.setUpdateTime(LocalDateTime.now());
        this.setVersion(0);
        this.setDeleted(false);
    }
}
