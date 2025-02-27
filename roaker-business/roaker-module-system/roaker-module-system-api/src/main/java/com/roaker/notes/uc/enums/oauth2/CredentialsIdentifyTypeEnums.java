package com.roaker.notes.uc.enums.oauth2;

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
public enum CredentialsIdentifyTypeEnums implements CommonEnum {
    PASSWORD(1, "Password"),
    PIN(2,"Pin");
    @EnumValue
    private final Integer code;

    private final String name;
}
