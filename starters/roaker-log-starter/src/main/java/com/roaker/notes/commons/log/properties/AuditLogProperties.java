package com.roaker.notes.commons.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 审计日志配置
 * @author lei.rao
 * @since 1.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "roaker.audit-log")
@RefreshScope
public class AuditLogProperties {
    /**
     * 是否开启审计日志
     */
    private Boolean enabled = true;
    /**
     * 日志记录类型(logger/redis/db/es)
     */
    private String logType = "db";
}