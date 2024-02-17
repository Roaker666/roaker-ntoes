package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DataScopeEnum implements IntArrayValuable, CommonEnum {

    ALL(1, "all"), // 全部数据权限

    DEPT_CUSTOM(2, "dept_custom"), // 指定部门数据权限
    DEPT_ONLY(3, "dept_only"), // 部门数据权限
    DEPT_AND_CHILD(4, "dept_and_child"), // 部门及以下数据权限

    SELF(5, "self"); // 仅本人数据权限

    /**
     * 范围
     */
    @EnumValue
    private final Integer code;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(DataScopeEnum::getCode).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

}