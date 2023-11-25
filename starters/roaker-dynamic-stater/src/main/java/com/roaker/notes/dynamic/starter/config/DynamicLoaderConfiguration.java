package com.roaker.notes.dynamic.starter.config;

import com.roaker.notes.dynamic.starter.core.DynamicApi;
import com.roaker.notes.dynamic.starter.loader.DynamicLoader;
import com.roaker.notes.dynamic.starter.loader.DynamicLoaderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lei.rao
 * @since 1.0
 */
@AutoConfiguration
@EnableScheduling // 开启调度任务的功能，因为 ErrorCodeRemoteLoader 通过定时刷新错误码
public class DynamicLoaderConfiguration {
    @Bean
    public DynamicLoader errorCodeLoader(@Value("${spring.application.name}") String applicationName,
                                         DynamicApi dynamicApi) {
        return new DynamicLoaderImpl(applicationName, dynamicApi);
    }
}
