package com.roaker.notes.dynamic.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface CommonEnum {
    static <T extends CommonEnum> T of(Integer code, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> Objects.equals(code, e.getCode()))
                .findFirst()
                .orElse(null);
    }

    static <T extends CommonEnum> T of(String name, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> StringUtils.equalsIgnoreCase(name, e.getName()))
                .findFirst()
                .orElse(null);
    }


    static <T extends CommonEnum> String nameOf(Integer code, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> Objects.equals(code, e.getCode()))
                .findFirst()
                .map(CommonEnum::getName)
                .orElse(null);
    }


    static <T extends CommonEnum> Integer codeOf(String name, Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> StringUtils.equalsIgnoreCase(name, e.getName()))
                .findFirst()
                .map(CommonEnum::getCode)
                .orElse(null);
    }

    Integer getCode();

    String getName();
}
