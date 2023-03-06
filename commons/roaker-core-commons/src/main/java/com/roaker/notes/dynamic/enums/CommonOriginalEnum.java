package com.roaker.notes.dynamic.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface CommonOriginalEnum {

    static <T extends CommonOriginalEnum> T originCodeOf(String code, Class<T> enumClass) {
        return EnumAssistant.originCodeOf(code, enumClass);
    }

    static <T extends CommonOriginalEnum> T originNameOf(String name, Class<T> enumClass) {
        return EnumAssistant.originNameOf(name, enumClass);
    }

    static <T extends CommonOriginalEnum> List<String> getOriginValues(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(T::getOriginName)
                .filter(e -> !"Undefined".equals(e))
                .collect(Collectors.toList());
    }

    /**
     * @return originCode
     */
    String getOriginCode();

    /**
     * @return originName
     */
    String getOriginName();
}
