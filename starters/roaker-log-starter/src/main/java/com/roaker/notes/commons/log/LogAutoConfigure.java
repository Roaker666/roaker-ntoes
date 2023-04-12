package com.roaker.notes.commons.log;

import com.roaker.notes.commons.log.aspect.AuditLogAspect;
import com.roaker.notes.commons.log.properties.AuditLogProperties;
import com.roaker.notes.commons.log.properties.LogDbProperties;
import com.roaker.notes.commons.log.properties.TraceProperties;
import com.roaker.notes.commons.log.service.impl.DbAuditServiceImpl;
import com.roaker.notes.commons.log.service.impl.LoggerAuditServiceImpl;
import com.zaxxer.hikari.HikariConfig;
import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 日志自动配置
 *
 * @author lei.rao
 * @since 1.0
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties({TraceProperties.class, AuditLogProperties.class})
public class LogAutoConfigure implements ApplicationContextAware {
    @Resource
    private DataSource dataSource;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 日志数据库配置
     */
    @Configuration
    @ConditionalOnClass(HikariConfig.class)
    @EnableConfigurationProperties(LogDbProperties.class)
    public static class LogDbAutoConfigure {
        @Autowired
        private DataSource dataSource;

        @Bean
        @ConditionalOnProperty(name = "roaker.audit-log.log-type", havingValue = "db")
        @ConditionalOnClass(JdbcTemplate.class)
        public AuditLogAspect dbAuditLogAspect(AuditLogProperties auditLogProperties, LogDbProperties logDbProperties) {
            DbAuditServiceImpl dbAuditService = new DbAuditServiceImpl(logDbProperties, dataSource);
            return new AuditLogAspect(auditLogProperties, dbAuditService);
        }
    }


    @Bean
    @ConditionalOnProperty(name = "roaker.audit-log.log-type", havingValue = "logger")
    public AuditLogAspect loggerAuditLogAspect(AuditLogProperties auditLogProperties) {
        LoggerAuditServiceImpl loggerAuditService = new LoggerAuditServiceImpl();
        return new AuditLogAspect(auditLogProperties, loggerAuditService);
    }


}
