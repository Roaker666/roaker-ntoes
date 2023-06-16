package com.roaker.notes.messaging.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class MessageImpl implements Message {
    private String payload;
    private Map<String, String> headers;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public Optional<String> getHeader(String name) {
        return Optional.ofNullable(headers.get(name));
    }

    @Override
    public String getRequiredHeader(String name) {
        String s = headers.get(name);
        if (s == null)
            throw new RuntimeException("No such header: " + name + " in this message " + this);
        else
            return s;
    }

    @Override
    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    @Override
    public String getId() {
        return getRequiredHeader(Message.ID);
    }

    @Override
    public void setHeader(String name, String value) {
        if (headers == null)
            headers = new HashMap<>();
        headers.put(name, value);
    }

    @Override
    public void removeHeader(String key) {
        headers.remove(key);
    }
}
