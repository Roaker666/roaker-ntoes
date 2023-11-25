package com.roaker.notes.messaging.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public class SubscriberIdAndMessage {
    private final String subscriberId;
    private final Message message;
}
