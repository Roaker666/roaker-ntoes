package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.Message;
import com.roaker.notes.messaging.common.SubscriberIdAndMessage;
import com.roaker.notes.messaging.consumer.MessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class DecoratedMessageHandlerFactory {
    private final List<MessageHandlerDecorator> decorators;

    public DecoratedMessageHandlerFactory(List<MessageHandlerDecorator> decorators) {
        decorators.sort(Comparator.comparingInt(MessageHandlerDecorator::getOrder));
        this.decorators = decorators;
    }

    public Consumer<SubscriberIdAndMessage> decorate(MessageHandler mh) {
        MessageHandlerDecoratorChainBuilder builder = new MessageHandlerDecoratorChainBuilder();
        for (MessageHandlerDecorator mhd : decorators) {
            builder = builder.andThen(mhd);
        }

        MessageHandlerDecoratorChain chain = builder.andFinally((smh) -> {
            String subscriberId = smh.getSubscriberId();
            Message message = smh.getMessage();
            try {
                log.trace("Invoking handler {} {}", subscriberId, message.getId());
                mh.accept(smh.getMessage());
                log.trace("handled message {} {}", subscriberId, message.getId());
            } catch (Exception e) {
                log.error("Got exception {} {}", subscriberId, message.getId());
                log.error("Got exception ", e);
                throw e;
            }

        });
        return chain::invokeNext;
    }
}
