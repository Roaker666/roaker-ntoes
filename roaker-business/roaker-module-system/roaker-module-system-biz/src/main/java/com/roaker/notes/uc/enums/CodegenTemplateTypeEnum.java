package com.roaker.notes.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.commons.utils.ObjectUtils;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum CodegenTemplateTypeEnum implements CommonEnum {

    CRUD(1, "crud"), // 单表（增删改查）
    TREE(2, "tree"), // 树表（增删改查）
    SUB(15, "sub"), // 主子表 - 子表
    MASTER_NORMAL(10, "MASTER_NORMAL"), // 主子表 - 主表 - 普通模式
    MASTER_ERP(11, "MASTER_ERP"), // 主子表 - 主表 - ERP 模式
    MASTER_INNER(12, "MASTER_INNER"), //主子表 - 主表 - 内嵌模式
    ;

    /**
     * 类型
     */
    @EnumValue
    private final Integer code;
    private final String name;



    /**
     * 是否为主表
     *
     * @param code 类型
     * @return 是否主表
     */
    public static boolean isMaster(Integer code) {
        return ObjectUtils.equalsAny(code,
                MASTER_NORMAL.code, MASTER_ERP.code, MASTER_INNER.code);
    }

    /**
     * 是否为树表
     *
     * @param code 类型
     * @return 是否树表
     */
    public static boolean isTree(Integer code) {
        return Objects.equals(code, TREE.code);
    }
}
