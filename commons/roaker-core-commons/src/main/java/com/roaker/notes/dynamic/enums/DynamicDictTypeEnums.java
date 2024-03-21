package com.roaker.notes.dynamic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum DynamicDictTypeEnums implements CommonEnum{
    /**
     * 配置类型;1:模板枚举配置 2:下拉筛选配置 3:国家编码配置
     */
    TEMPLATE(1, "模板枚举配置"),
    CHECK_BOX(2, "下拉筛选配置"),
    COUNTRY_CODE(3, "国家码"),
    PARAM_CONFIG(4, "参数配置"),
    ERROR_CODE(5, "错误码");
    @EnumValue
    private final Integer code;
    private final String name;

    public static DynamicDictTypeEnums codeOf(Integer type) {
        return Arrays.stream(DynamicDictTypeEnums.values())
                .filter(dynamicDictTypeEnums -> dynamicDictTypeEnums.getCode().equals(type))
                .findFirst()
                .orElseThrow();
    }

    public static DynamicDictTypeEnums codeOf(String type) {
        return Arrays.stream(DynamicDictTypeEnums.values())
                .filter(dynamicDictTypeEnums -> dynamicDictTypeEnums.getCode().equals(Integer.parseInt(type)))
                .findFirst()
                .orElseThrow();
    }
}
