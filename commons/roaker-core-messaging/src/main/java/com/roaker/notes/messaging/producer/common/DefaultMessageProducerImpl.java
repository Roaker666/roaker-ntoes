package com.roaker.notes.messaging.producer.common;

import com.roaker.notes.messaging.common.ChannelMapping;
import com.roaker.notes.messaging.common.Message;
import com.roaker.notes.messaging.common.MessageInterceptor;
import com.roaker.notes.messaging.producer.HttpDateHeaderFormatUtil;
import com.roaker.notes.messaging.producer.MessageProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class DefaultMessageProducerImpl implements MessageProducer {
    private final MessageInterceptor[] messageInterceptors;
    private final ChannelMapping channelMapping;
    private final MessageProducerImplementation implementation;

    @Override
    public void send(String destination, Message message) {
        prepareMessageHeaders(destination, message);
        implementation.withContext(() -> send(message));
    }

    protected void prepareMessageHeaders(String destination, Message message) {
        implementation.setMessageIdIfNecessary(message);
        message.getHeaders().put(Message.DESTINATION, channelMapping.transform(destination));
        message.getHeaders().put(Message.DATE, HttpDateHeaderFormatUtil.nowAsHttpDateString());
        if (message.getHeaders().get(Message.PARTITION_ID) == null)
            message.getHeaders().put(Message.PARTITION_ID, UUID.randomUUID().toString());
    }

    private void preSend(Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.preSend(message));
    }

    private void postSend(Message message, RuntimeException e) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.postSend(message, e));
    }

    protected void send(Message message) {
        preSend(message);
        try {
            implementation.send(message);
            postSend(message, null);
        } catch (RuntimeException e) {
            log.error("Sending failed", e);
            postSend(message, e);
            throw e;
        }
    }
}
