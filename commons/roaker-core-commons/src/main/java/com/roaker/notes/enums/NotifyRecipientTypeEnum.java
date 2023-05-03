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
public enum NotifyRecipientTypeEnum implements CommonEnum {
    NON_USER(1, "non-user"),
    SPECIAL_USER(2, "special-user");
    @EnumValue
    private final Integer code;
    private final String name;
}
