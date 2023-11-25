package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态枚举
 *
 * @author lei.rao
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements CommonEnum, IntArrayValuable {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(CommonStatusEnum::getCode).toArray();
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
        return new int[0];
    }

}
