package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 显示状态枚举
 *
 * @author lei.rao
 */
@Getter
@AllArgsConstructor
public enum ShowStatusEnum implements CommonEnum, IntArrayValuable {

    SHOW(1, "显示"),
    DIS_SHOW(0, "不显示");
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ShowStatusEnum::getCode).toArray();
    /**
     * 状态值
     */
    @EnumValue
    private final Integer code;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
