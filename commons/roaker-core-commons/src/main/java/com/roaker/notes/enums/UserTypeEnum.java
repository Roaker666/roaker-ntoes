package com.roaker.notes.enums;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 全局用户类型枚举
 */
@AllArgsConstructor
@Getter
public enum UserTypeEnum implements CommonEnum, IntArrayValuable {

    MEMBER(1, "会员"), // 面向 c 端，普通用户
    ADMIN(2, "管理员"); // 面向 b 端，管理后台

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(UserTypeEnum::getCode).toArray();

    /**
     * 类型
     */
    @EnumValue
    private final Integer code;
    /**
     * 类型名
     */
    private final String name;

    public static UserTypeEnum valueOf(Integer code) {
        return ArrayUtil.firstMatch(userType -> userType.getCode().equals(code), UserTypeEnum.values());
    }

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
