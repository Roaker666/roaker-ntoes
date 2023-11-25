package com.roaker.notes.uc.enums.notify;

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
public enum MessageSendStatusEnum implements CommonEnum {
    WAITING_SEND(0, "等待发送"),
    SENDING(1, "发送中"),
    RETRY_SENDING(2,"重试发送中"),
    SEND_SUCCESS(100, "发送成功"),
    SEND_FAILURE(101, "发送失败 ");
    @EnumValue
    private final Integer code;

    private final String name;
}
