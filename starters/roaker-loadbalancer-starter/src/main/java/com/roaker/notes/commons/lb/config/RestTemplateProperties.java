package com.roaker.notes.commons.lb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lei.rao
 * @since 1.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "roaker.rest-template")
public class RestTemplateProperties {
    /**
     * 最大链接数
     */
    private int maxTotal = 200;
    /**
     * 读取超时时间 ms
     */
    private int readTimeout = 35000;
    /**
     * 读取超时时间 ms
     */
    private int writeTimeout = 35000;
    /**
     * 链接超时时间 ms
     */
    private int connectTimeout= 10000;
    /**
     * 连接空闲时间最多 ms
     */
    private int keepAliveDuration = 300000;
}
