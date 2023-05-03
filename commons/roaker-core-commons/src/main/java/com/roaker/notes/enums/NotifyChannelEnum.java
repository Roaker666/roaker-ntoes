package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotifyChannelEnum implements CommonEnum {
    PN(1, "PN"),
    AR(2, "AR"),
    SMS(3, "SMS"),
    MAIL(4, "MAIL");
    @EnumValue
    private final Integer code;
    private final String name;
}
