package com.roaker.notes.starters.sms.core.client.impl.debug;

import com.roaker.notes.exception.ErrorCode;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.starters.sms.core.client.SmsCodeMapping;
import com.roaker.notes.starters.sms.core.property.SmsFramewordErrorCodeConstants;

import java.util.Objects;

/**
 * @author lei.rao
 * @since 1.0
 */
public class DebugDingTalkCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return Objects.equals(apiCode, "0") ? GlobalErrorCodeConstants.SUCCESS : SmsFramewordErrorCodeConstants.SMS_UNKNOWN;
    }

}
