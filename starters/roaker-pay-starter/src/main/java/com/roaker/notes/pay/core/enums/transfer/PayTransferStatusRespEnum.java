package com.roaker.notes.pay.core.enums.transfer;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum PayTransferStatusRespEnum {
    WAITING(0, "等待转账"),

    /**
     * TODO 转账到银行卡. 会有T+0 T+1 到账的请情况。 还未实现
     * TODO @jason：可以看看其它开源项目，针对这个场景，处理策略是怎么样的？例如说，每天主动轮询？这个状态的单子？
     */
    IN_PROGRESS(10, "转账进行中"),

    SUCCESS(20, "转账成功"),
    /**
     * 转账关闭 (失败，或者其它情况)
     */
    CLOSED(30, "转账关闭");
    @EnumValue
    private final Integer code;
    private final String name;
    public static List<PayTransferStatusRespEnum> getListByCodeList(List<Integer> list) {
        return Arrays.stream(values())
                .filter(payTransferStatusEnum -> list.contains(payTransferStatusEnum.getCode()))
                .collect(Collectors.toList());
    }

    public static boolean isSuccess(Integer status) {
        return Objects.equals(status, SUCCESS.getCode());
    }

    public static boolean isClosed(Integer status) {
        return Objects.equals(status, CLOSED.getCode());
    }

    public static boolean isWaiting(Integer status) {
        return Objects.equals(status, WAITING.getCode());
    }

    public static boolean isInProgress(Integer status) {
        return Objects.equals(status, IN_PROGRESS.getCode());
    }

    /**
     * 是否处于待转账或者转账中的状态
     *
     * @param status 状态
     */
    public static boolean isPendingStatus(Integer status) {
        return Objects.equals(status, WAITING.getCode()) || Objects.equals(status, IN_PROGRESS.getCode());
    }
}
