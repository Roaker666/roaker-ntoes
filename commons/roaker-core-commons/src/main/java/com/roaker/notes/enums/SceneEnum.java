package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SceneEnum implements CommonEnum {
    ALL(1, "ALL");
    @EnumValue
    private final Integer code;
    private final String name;
}
