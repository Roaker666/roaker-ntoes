package com.roaker.notes.messaging.producer.common;

import com.roaker.notes.messaging.common.Message;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface MessageProducerImplementation {

    void send(Message message);

    default void setMessageIdIfNecessary(Message message) {
        //do nothing by default
    }

    default void withContext(Runnable runnable) {
        runnable.run();
    }
}
