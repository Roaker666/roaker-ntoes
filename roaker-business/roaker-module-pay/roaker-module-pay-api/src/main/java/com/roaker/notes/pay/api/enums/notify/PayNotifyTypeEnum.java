package com.roaker.notes.pay.api.enums.notify;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通知类型
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayNotifyTypeEnum implements CommonEnum {

    ORDER(1, "支付单"),
    REFUND(2, "退款单"),
    TRANSFER(3, "转账单")
    ;

    /**
     * 类型
     */
    @EnumValue
    private final Integer code;
    /**
     * 名字
     */
    private final String name;

}
