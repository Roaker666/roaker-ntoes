package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.common.value.qual.EnumVal;

import java.util.Arrays;

/**
 * 通用状态枚举
 *
 * @author lei.rao
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements CommonEnum {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");
    /**
     * 状态值
     */
    @EnumValue
    private final Integer code;
    /**
     * 状态名
     */
    private final String name;

}
