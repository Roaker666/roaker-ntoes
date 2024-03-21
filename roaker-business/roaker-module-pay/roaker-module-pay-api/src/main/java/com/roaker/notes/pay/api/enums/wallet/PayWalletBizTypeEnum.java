package com.roaker.notes.pay.api.enums.wallet;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 钱包交易业务分类
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum PayWalletBizTypeEnum implements CommonEnum, IntArrayValuable {

    RECHARGE(1, "充值"),
    RECHARGE_REFUND(2, "充值退款"),
    PAYMENT(3, "支付"),
    PAYMENT_REFUND(4, "支付退款");

    /**
     * 业务分类
     */
    @EnumValue
    private final Integer code;
    /**
     * 说明
     */
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PayWalletBizTypeEnum::getCode).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}

