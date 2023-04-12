package com.roaker.notes.infra.encrypt.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum KeyTypeEnums implements CommonEnum {
    KEY_PIN(1, "key for PIN"),
    KEY_PW(2, "key for password"),
    KEY_DATA(3, "key for data");
    @EnumValue
    private final Integer code;
    private final String name;

    public static KeyTypeEnums fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(keyTypeEnums -> keyTypeEnums.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new ServerException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR));
    }
}
