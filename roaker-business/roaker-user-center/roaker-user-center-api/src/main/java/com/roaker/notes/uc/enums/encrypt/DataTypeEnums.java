package com.roaker.notes.uc.enums.encrypt;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum DataTypeEnums implements CommonEnum {
    DEFAULT(0, "默认"),
    MOBILE(1, "手机号码"),
    ID_NO(2, "证件号"),
    CARD_NO(3, "银行卡号"),
    OTHER_ACCOUNT_NO(4, "其他卡号,如购物卡号"),
    EMAIL(5, "邮箱"),
    SPECIAL_KEY(6, "特殊key"),
    USERNAME(7, "用户名"),
    URL(8, "URL地址"),
    OTHER(9, "其他"),
    PIN(100, "PIN"),
    PASSWORD(200, "PASSWORD");

    @EnumValue
    private final Integer code;
    private final String name;

    public static DataTypeEnums fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(dataTypeEnums -> dataTypeEnums.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new ServerException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR));
    }
}
