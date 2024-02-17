package com.roaker.notes.uc.enums;

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
public enum CodegenFrontTypeEnum implements CommonEnum {

    VUE2(10, "vue2"), // Vue2 Element UI 标准模版
    VUE3(20, "vue3"), // Vue3 Element Plus 标准模版
    VUE3_SCHEMA(21, "vue3_schema"), // Vue3 Element Plus Schema 模版
    VUE3_VBEN(30, "vue3_vben"), // Vue3 VBEN 模版
    ;

    /**
     * 类型
     */
    @EnumValue
    private final Integer code;
    private final String name;
}
