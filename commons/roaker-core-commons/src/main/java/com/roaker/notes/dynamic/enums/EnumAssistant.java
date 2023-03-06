package com.roaker.notes.dynamic.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumAssistant {

    public static <T extends CommonOriginalEnum> T originCodeOf(String code, Class<T> enumClass) {
        if (StringUtils.isBlank(code)) {
            log.warn("Get enum null since origin code[{}] is blank.", code);
            return null;
        }

        for (T e : enumClass.getEnumConstants()) {
            Set<String> codeSets = Arrays.stream(e.getOriginCode().split(",")).collect(Collectors.toSet());
            if (codeSets.contains(code)) {
                log.debug("Get enum[{}]:[{}] from origin code[{}].", enumClass.getSimpleName(), ((Enum) e).name(), code);
                return e;
            }
        }
        log.warn("Can not get enum[{}] from origin code[{}].", enumClass.getSimpleName(), code);
        return null;
    }

    public static <T extends CommonOriginalEnum> T originNameOf(String name, Class<T> enumClass) {
        for (T e : enumClass.getEnumConstants()) {
            if (e.getOriginName().equalsIgnoreCase(name)) {
                log.debug("Get enum[{}]:[{}] from origin name[{}].", enumClass.getSimpleName(), ((Enum) e).name(), name);
                return e;
            }
        }
        log.warn("Can not get enum[{}] from origin name[{}].", enumClass.getSimpleName(), name);
        return null;
    }
}