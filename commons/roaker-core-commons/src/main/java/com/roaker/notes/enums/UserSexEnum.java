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
public enum UserSexEnum implements CommonEnum {
    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(3, "未知");
    @EnumValue
    private final Integer code;
    private final String name;
}
