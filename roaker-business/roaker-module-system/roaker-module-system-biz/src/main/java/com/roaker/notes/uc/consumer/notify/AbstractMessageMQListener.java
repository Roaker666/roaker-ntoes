package com.roaker.notes.uc.consumer.notify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.roaker.notes.commons.utils.JacksonUtils;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.slf4j.Logger;

/**
 * @author lei.rao
 * @since 1.0
 */
public abstract class AbstractMessageMQListener<T> implements MessageListener<T> {
    protected abstract Logger log();

    protected abstract String logPrefix();


    @Override
    public void received(Consumer<T> consumer, Message<T> msg) {
        log().info("{} receive message from rocketmq: msgId={}", logPrefix(), msg.getMessageId());
        String msgBody = new String(msg.getData());
        try {
            T data = JacksonUtils.from(msgBody, new TypeReference<T>() {
            });
            doHandleMessage(data);
        } catch (Exception e) {
            log().error("{} handle message error", logPrefix(), e);
            //TODO 上报监控
            throw e;
        }
    }

    protected abstract void doHandleMessage(T msg);
}
