package com.roaker.notes.pay.api.enums.transfer;

import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PayTransferStatusEnum implements CommonEnum {

    WAITING(0, "等待转账"),
    /**
     * TODO 转账到银行卡. 会有T+0 T+1 到账的请情况。 还未实现
     */
    IN_PROGRESS(10, "转账进行中"),

    SUCCESS(20, "转账成功"),
    /**
     * 转账关闭 (失败，或者其它情况) // TODO 改成 转账失败状态
     */
    CLOSED(30, "转账关闭");

    /**
     * 状态
     */
    private final Integer code;
    /**
     * 状态名
     */
    private final String name;

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

    public static Object getListByCodeList(List<Integer> status) {
        return Arrays.stream(values())
                .filter(payTransferStatusEnum -> status.contains(payTransferStatusEnum.getCode()))
                .collect(Collectors.toList());
    }
}
