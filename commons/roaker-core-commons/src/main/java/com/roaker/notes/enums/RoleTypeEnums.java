package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnums implements CommonEnum {

    /**
     * 内置角色
     */
    SYSTEM(1, "system"),
    /**
     * 自定义角色
     */
    CUSTOM(2, "custom");

    @EnumValue
    private final Integer code;
    private final String name;
}
