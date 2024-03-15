package com.roaker.notes.pay.core.enums.order;

import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayOrderStatusRespEnum implements CommonEnum {
    WAITING(0, "未支付"),
    SUCCESS(10, "支付成功"),
    REFUND(20, "已退款"),
    CLOSED(30, "支付关闭"),
    ;
    private final Integer code;
    private final String name;

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
     * 判断是否已退款
     *
     * @param status 状态
     * @return 是否支付成功
     */
    public static boolean isRefund(Integer status) {
        return Objects.equals(status, REFUND.getCode());
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
