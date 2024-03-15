package com.roaker.notes.uc.api.seq;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface SeqApi {
    String getSegmentId(@PathVariable("key") String key);
}
