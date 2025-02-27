package com.roaker.notes.commons.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author lei.rao
 * @since 1.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "roaker.trace")
@RefreshScope
public class TraceProperties {
    /**
     * 是否开启日志链路追踪
     */
    private Boolean enable = false;
}
