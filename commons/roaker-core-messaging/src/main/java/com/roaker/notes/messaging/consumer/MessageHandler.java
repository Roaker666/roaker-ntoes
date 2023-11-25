package com.roaker.notes.messaging.consumer;

import com.roaker.notes.messaging.common.Message;

import java.util.function.Consumer;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface MessageHandler extends Consumer<Message> {
}
