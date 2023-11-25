package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.SubscriberIdAndMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author lei.rao
 * @since 1.0
 */
public class MessageHandlerDecoratorChainBuilder {
    private final List<MessageHandlerDecorator> handlers = new LinkedList<>();

    private void add(MessageHandlerDecorator smh) {
        this.handlers.add(smh);
    }

    public MessageHandlerDecoratorChainBuilder andThen(MessageHandlerDecorator smh) {
        this.add(smh);
        return this;
    }

    public MessageHandlerDecoratorChain andFinally(Consumer<SubscriberIdAndMessage> consumer) {
        return buildChain(handlers, consumer);
    }

    private MessageHandlerDecoratorChain buildChain(List<MessageHandlerDecorator> handlers, Consumer<SubscriberIdAndMessage> consumer) {
        if  (handlers.isEmpty())
            return consumer::accept;
        else {
            MessageHandlerDecorator head = handlers.get(0);
            List<MessageHandlerDecorator> tail = handlers.subList(1, handlers.size());
            return subscriberIdAndMessage -> head.accept(subscriberIdAndMessage, buildChain(tail, consumer));
        }
    }
}
