package com.roaker.notes.pay.api.enums.refund;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 渠道的退款状态枚举
 *
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayRefundStatusEnum implements CommonEnum {

    WAITING(0, "未退款"),
    SUCCESS(10, "退款成功"),
    FAILURE(20, "退款失败");

    @EnumValue
    private final Integer code;
    private final String name;

    public static boolean isSuccess(Integer status) {
        return Objects.equals(status, SUCCESS.getCode());
    }

    public static boolean isFailure(Integer status) {
        return Objects.equals(status, FAILURE.getCode());
    }
}

