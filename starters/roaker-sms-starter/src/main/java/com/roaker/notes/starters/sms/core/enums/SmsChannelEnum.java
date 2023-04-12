package com.roaker.notes.starters.sms.core.enums;

import cn.hutool.core.util.ArrayUtil;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum SmsChannelEnum implements CommonEnum {
    DEBUG_DING_TALK(1, "调试(钉钉)"),
    ALIYUN(2, "阿里云"),
    TENCENT(3, "腾讯云");
    //    HUA_WEI("HUA_WEI", "华为云"),
    private final Integer code;
    private final String name;

    public static SmsChannelEnum getByCode(Integer code) {
        return ArrayUtil.firstMatch(o -> o.getCode().equals(code), values());
    }
}
