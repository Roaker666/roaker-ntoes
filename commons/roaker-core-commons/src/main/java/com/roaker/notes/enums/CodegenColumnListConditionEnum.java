package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum CodegenColumnListConditionEnum implements CommonEnum {
    EQ(1, "="),
    NE(2, "!="),
    GT(3, ">"),
    GTE(4, ">="),
    LT(5, "<"),
    LTE(6, "<="),
    LIKE(7, "LIKE"),
    BETWEEN(8, "BETWEEN");
    @EnumValue
    private final Integer code;
    private final String name;
}
