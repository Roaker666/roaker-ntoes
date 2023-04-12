package com.roaker.notes.seq;

import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lei.rao
 * @since 1.0
 */
@FeignClient(name = "seq-service", fallbackFactory = SeqClient.SeqClientFallbackFactory.class)
public interface SeqClient {
    @RequestMapping(value = "/api/segment/get/{key}")
    String getSegmentId(@PathVariable("key") String key);

    @Component
    class SeqClientFallbackFactory implements FallbackFactory<SeqClient> {

        @Override
        public SeqClient create(Throwable cause) {
            return key -> {
                throw new ServerException(GlobalErrorCodeConstants.SEQ_SERVER_ERROR);
            };
        }
    }
}
