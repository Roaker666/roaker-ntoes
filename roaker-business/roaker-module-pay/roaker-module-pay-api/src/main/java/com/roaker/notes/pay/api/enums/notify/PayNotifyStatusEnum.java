package com.roaker.notes.pay.api.enums.notify;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通知状态枚举
 *
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayNotifyStatusEnum implements CommonEnum {

    WAITING(0, "等待通知"),
    SUCCESS(10, "通知成功"),
    FAILURE(20, "通知失败"), // 多次尝试，彻底失败
    REQUEST_SUCCESS(21, "请求成功，但是结果失败"),
    REQUEST_FAILURE(22, "请求失败"),
    ;
    /**
     * 状态
     */
    @EnumValue
    private final Integer code;
    /**
     * 名字
     */
    private final String name;

}

