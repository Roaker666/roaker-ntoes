package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.SubscriberIdAndMessage;

import java.util.function.BiConsumer;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface MessageHandlerDecorator extends BiConsumer<SubscriberIdAndMessage, MessageHandlerDecoratorChain> {
    int getOrder();
}
