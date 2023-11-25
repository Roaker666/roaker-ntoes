package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.SubscriberIdAndMessage;
import com.roaker.notes.messaging.consumer.BuiltInMessageHandlerDecoratorOrder;
import lombok.AllArgsConstructor;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
public class DuplicateDetectingMessageHandlerDecorator implements MessageHandlerDecorator {
    private DuplicateMessageDetector duplicateMessageDetector;
    @Override
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        duplicateMessageDetector.doWithMessage(subscriberIdAndMessage, () -> messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage));
    }

    @Override
    public int getOrder() {
        return BuiltInMessageHandlerDecoratorOrder.DUPLICATE_DETECTING_MESSAGE_HANDLER_DECORATOR;
    }
}
