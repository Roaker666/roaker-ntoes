package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum RoleCodeEnum implements CommonEnum {

    SUPER_ADMIN(1, "超级管理员"),
    TENANT_ADMIN(2, "租户管理员"),
    ;

    /**
     * 角色编码
     */
    @EnumValue
    private final Integer code;
    /**
     * 名字
     */
    private final String name;

    public static boolean isSuperAdmin(String code) {
        return Objects.equals(Integer.valueOf(code), SUPER_ADMIN.getCode());
    }

}