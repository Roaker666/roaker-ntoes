package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.consumer.MessageHandler;
import com.roaker.notes.messaging.consumer.MessageSubscription;

import java.util.Set;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface MessageConsumerImplementation {
    MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler);

    String getId();

    void close();
}
