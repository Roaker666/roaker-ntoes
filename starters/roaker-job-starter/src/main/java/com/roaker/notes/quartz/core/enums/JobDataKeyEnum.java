package com.roaker.notes.quartz.core.enums;

import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum JobDataKeyEnum implements CommonEnum {
    JOB_ID(1, "JOB_ID"),
    JOB_HANDLER_NAME(2, "JOB_HANDLER_NAME"),
    JOB_HANDLER_PARAM(3, "JOB_HANDLER_PARAM"),
    JOB_RETRY_COUNT(4, "JOB_RETRY_COUNT"), // 最大重试次数
    JOB_RETRY_INTERVAL(5, "JOB_RETRY_INTERVAL"); // 每次重试间隔
    private final Integer code;
    private final String name;
}
