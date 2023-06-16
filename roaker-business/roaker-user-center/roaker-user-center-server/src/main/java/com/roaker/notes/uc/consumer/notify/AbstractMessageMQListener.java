package com.roaker.notes.uc.consumer.notify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.roaker.notes.commons.utils.JacksonUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;

/**
 * @author lei.rao
 * @since 1.0
 */
public abstract class AbstractMessageMQListener<T> implements RocketMQListener<MessageExt> {
    protected abstract Logger log();

    protected abstract String logPrefix();

    @Override
    public void onMessage(MessageExt messageExt) {
        log().info("{} receive message from rocketmq: msgId={}", logPrefix(), messageExt.getMsgId());
        String msgBody = new String(messageExt.getBody());
        try {
            T msg = JacksonUtils.from(msgBody, new TypeReference<T>() {
            });
            doHandleMessage(msg);
        } catch (Exception e) {
            log().error("{} handle message error", logPrefix(), e);
            //TODO 上报监控
            throw e;
        }
    }

    protected abstract void doHandleMessage(T msg);
}
