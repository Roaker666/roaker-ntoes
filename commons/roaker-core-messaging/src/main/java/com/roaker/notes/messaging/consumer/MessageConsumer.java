package com.roaker.notes.messaging.consumer;

import java.util.Set;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface MessageConsumer {
    MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler);

    String getId();

    void close();
}
