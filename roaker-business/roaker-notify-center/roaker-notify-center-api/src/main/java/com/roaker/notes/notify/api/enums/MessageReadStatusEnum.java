package com.roaker.notes.notify.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum MessageReadStatusEnum implements CommonEnum {
    UNREAD(0,"未读"),
    READ(1,"已读");
    @EnumValue
    private final Integer code;
    private final String name;
}
