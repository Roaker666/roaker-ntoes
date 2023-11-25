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
public class PrePostHandlerMessageHandlerDecorator implements MessageHandlerDecorator {
    private MessageInterceptor[] messageInterceptors;

    @Override
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        Message message = subscriberIdAndMessage.getMessage();
        String subscriberId = subscriberIdAndMessage.getSubscriberId();
        preHandle(subscriberId, message);
        try {
            messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
            postHandle(subscriberId, message, null);
        } catch (Throwable t) {
            log.error("decoration failed", t);
            postHandle(subscriberId, message, t);
            throw t;
        }

    }

    private void preHandle(String subscriberId, Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.preHandle(subscriberId, message));
    }

    private void postHandle(String subscriberId, Message message, Throwable t) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.postHandle(subscriberId, message, t));
    }

    @Override
    public int getOrder() {
        return BuiltInMessageHandlerDecoratorOrder.PRE_POST_HANDLER_MESSAGE_HANDLER_DECORATOR;
    }
}
