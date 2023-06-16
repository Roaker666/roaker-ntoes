package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.SubscriberIdAndMessage;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface DuplicateMessageDetector {
    boolean isDuplicate(String consumerId, String messageId);

    void doWithMessage(SubscriberIdAndMessage subscriberIdAndMessage, Runnable callback);
}
