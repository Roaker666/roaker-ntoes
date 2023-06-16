package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.SubscriberIdAndMessage;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface MessageHandlerDecoratorChain {
    void invokeNext(SubscriberIdAndMessage subscriberIdAndMessage);
}
