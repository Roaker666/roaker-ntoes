package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.SubscriberIdAndMessage;

/**
 * @author lei.rao
 * @since 1.0
 */
public class NoopDuplicateMessageDetector implements DuplicateMessageDetector{
    @Override
    public boolean isDuplicate(String consumerId, String messageId) {
        return false;
    }

    @Override
    public void doWithMessage(SubscriberIdAndMessage subscriberIdAndMessage, Runnable callback) {
        callback.run();
    }
}
