package com.roaker.notes.commons.web;

import com.roaker.notes.commons.web.handler.GlobalExceptionHandler;
import com.roaker.notes.commons.web.handler.GlobalResponseBodyHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lei.rao
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class RoakerWebAutoConfiguration implements WebMvcConfigurer {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public GlobalResponseBodyHandler globalResponseBodyHandler() {
        return new GlobalResponseBodyHandler();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler(applicationName);
    }
}
