package com.roaker.notes.dynamic.starter.core;

import com.roaker.notes.dynamic.enums.DynamicDictDO;
import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Component
@Slf4j
public class DynamicApiFallback implements FallbackFactory<DynamicApi> {
    @Override
    public DynamicApi create(Throwable throwable) {
        return new DynamicApi() {
            @Override
            public List<DynamicDictDO> getDynamicDictList(LocalDateTime minUpdateTime) {
                log.info("Encrypt Platform client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.ENCRYPT_SERVER_ERROR);
            }
        };
    }
}
