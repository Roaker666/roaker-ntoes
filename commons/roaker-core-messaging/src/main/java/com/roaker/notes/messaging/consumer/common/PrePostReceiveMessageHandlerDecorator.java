package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.Message;
import com.roaker.notes.messaging.common.MessageInterceptor;
import com.roaker.notes.messaging.common.SubscriberIdAndMessage;
import com.roaker.notes.messaging.consumer.BuiltInMessageHandlerDecoratorOrder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class PrePostReceiveMessageHandlerDecorator implements MessageHandlerDecorator {
    private MessageInterceptor[] messageInterceptors;

    @Override
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        Message message = subscriberIdAndMessage.getMessage();
        preReceive(message);
        try {
            messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
        } finally {
            postReceive(message);
        }
    }

    private void preReceive(Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.preReceive(message));
    }


    private void postReceive(Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.postReceive(message));
    }

    @Override
    public int getOrder() {
        return BuiltInMessageHandlerDecoratorOrder.PRE_POST_RECEIVE_MESSAGE_HANDLER_DECORATOR;
    }
}
