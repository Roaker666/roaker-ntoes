package com.roaker.notes.pay.api.enums.order;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.commons.utils.ObjectUtils;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 支付订单的状态枚举
 *
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayOrderStatusEnum implements CommonEnum, IntArrayValuable {

    WAITING(0, "未支付"),
    SUCCESS(10, "支付成功"),
    REFUND(20, "已退款"),
    CLOSED(30, "支付关闭"), // 注意：全部退款后，还是 REFUND 状态
    ;

    @EnumValue
    private final Integer code;
    private final String name;

    @Override
    public int[] array() {
        return new int[0];
    }

    /**
     * 判断是否支付成功
     *
     * @param status 状态
     * @return 是否支付成功
     */
    public static boolean isSuccess(Integer status) {
        return Objects.equals(status, SUCCESS.getCode());
    }

    /**
     * 判断是否支付成功或者已退款
     *
     * @param status 状态
     * @return 是否支付成功或者已退款
     */
    public static boolean isSuccessOrRefund(Integer status) {
        return ObjectUtils.equalsAny(status,
                SUCCESS.getCode(), REFUND.getCode());
    }

    /**
     * 判断是否支付关闭
     *
     * @param status 状态
     * @return 是否支付关闭
     */
    public static boolean isClosed(Integer status) {
        return Objects.equals(status, CLOSED.getCode());
    }

}