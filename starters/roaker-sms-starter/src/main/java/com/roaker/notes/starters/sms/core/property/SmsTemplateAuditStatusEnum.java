package com.roaker.notes.starters.sms.core.property;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信模板的审核状态枚举
 *
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum SmsTemplateAuditStatusEnum {

    CHECKING(1),
    SUCCESS(2),
    FAIL(3);

    private final Integer status;
}
