package com.roaker.notes.messaging.common;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Builder
public class DefaultChannelMapping implements ChannelMapping {
    private final Map<String, String> mappings;

    @Override
    public String transform(String channel) {
        return mappings.getOrDefault(channel, channel);
    }
}
