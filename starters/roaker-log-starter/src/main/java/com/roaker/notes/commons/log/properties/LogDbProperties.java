package com.roaker.notes.commons.log.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lei.rao
 * @since 1.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "roaker.audit-log.datasource")
public class LogDbProperties extends HikariConfig {
}

