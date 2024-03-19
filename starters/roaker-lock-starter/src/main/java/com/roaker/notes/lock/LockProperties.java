package com.roaker.notes.lock;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author lei.rao
 * @since 1.0
 */
@ConfigurationProperties(prefix = "roaker.lock")
@RefreshScope
@Data
public class LockProperties {
    private String type;
    private String lockPrefix;
    private Long expireTime;
}
