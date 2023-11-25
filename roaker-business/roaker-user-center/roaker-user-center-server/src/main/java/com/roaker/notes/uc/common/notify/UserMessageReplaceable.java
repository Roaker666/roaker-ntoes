package com.roaker.notes.uc.common.notify;

import java.util.function.Function;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface UserMessageReplaceable {
    void replaceContent(Function<String, String> resolver);
}
