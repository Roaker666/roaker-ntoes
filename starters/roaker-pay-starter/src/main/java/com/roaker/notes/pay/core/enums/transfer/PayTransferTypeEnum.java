package com.roaker.notes.pay.core.enums.transfer;

import cn.hutool.core.util.ArrayUtil;
import com.roaker.notes.commons.core.IntArrayValuable;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayTransferTypeEnum implements CommonEnum, IntArrayValuable {
    ALIPAY_BALANCE(1, "支付宝余额"),
    WX_BALANCE(2, "微信余额"),
    BANK_CARD(3, "银行卡"),
    WALLET_BALANCE(4, "钱包余额");

    private final Integer code;
    private final String name;

    public interface WxPay {
    }

    public interface Alipay {
    }


    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PayTransferTypeEnum::getCode).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static PayTransferTypeEnum typeOf(Integer type) {
        return ArrayUtil.firstMatch(item -> item.getCode().equals(type), values());
    }

}
