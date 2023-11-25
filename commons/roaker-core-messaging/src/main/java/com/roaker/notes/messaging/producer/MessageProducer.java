package com.roaker.notes.messaging.producer;

import com.roaker.notes.messaging.common.Message;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface MessageProducer {

    /**
     * Send a message
     * @param destination the destination channel
     * @param message the message to doSend
     * @see Message
     */
    void send(String destination, Message message);

}
