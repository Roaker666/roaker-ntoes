package com.roaker.notes.messaging.consumer.common;

import com.roaker.notes.messaging.common.ChannelMapping;
import com.roaker.notes.messaging.common.SubscriberIdAndMessage;
import com.roaker.notes.messaging.consumer.MessageConsumer;
import com.roaker.notes.messaging.consumer.MessageHandler;
import com.roaker.notes.messaging.consumer.MessageSubscription;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class MessageConsumerImpl implements MessageConsumer {
    private ChannelMapping channelMapping;

    private MessageConsumerImplementation target;

    private DecoratedMessageHandlerFactory decoratedMessageHandlerFactory;

    @Override
    public MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler) {
        log.info("Subscribing: subscriberId = {}, channels = {}", subscriberId, channels);
        Consumer<SubscriberIdAndMessage> decorateHandler = decoratedMessageHandlerFactory.decorate(handler);
        MessageSubscription messageSubscription = target.subscribe(subscriberId,
                channels.stream().map(channelMapping::transform).collect(Collectors.toSet()),
                message -> decorateHandler.accept(new SubscriberIdAndMessage(subscriberId, message)));
        log.info("Subscribed: subscriberId = {}, channels = {}", subscriberId, channels);
        return messageSubscription;
    }

    @Override
    public String getId() {
        return target.getId();
    }

    @Override
    public void close() {
        log.info("Closing consumer");
        target.close();
        log.info("Closed consumer");
    }
}
