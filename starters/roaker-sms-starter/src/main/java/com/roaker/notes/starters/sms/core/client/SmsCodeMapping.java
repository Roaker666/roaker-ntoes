package com.roaker.notes.starters.sms.core.client;

import com.roaker.notes.exception.ErrorCode;

import java.util.function.Function;

/**
 * @author lei.rao
 * @since 1.0
 */

public interface SmsCodeMapping extends Function<String, ErrorCode> {
}
