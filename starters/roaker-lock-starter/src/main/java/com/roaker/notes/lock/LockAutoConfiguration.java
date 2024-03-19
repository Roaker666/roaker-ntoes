package com.roaker.notes.lock;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;

import javax.sql.DataSource;

/**
 * @author lei.rao
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(LockProperties.class)
public class LockAutoConfiguration {

    @Bean
    public RoakerLockUtils roakerLockUtils(LockRegistry lockRegistry) {
        return new RoakerLockUtils(lockRegistry);
    }

    @Bean
    @ConditionalOnProperty(prefix = "roaker.lock", name = "type", havingValue = "db")
    public DefaultLockRepository defaultLockRepository(DataSource dataSource) {
        return new DefaultLockRepository(dataSource);
    }

    @Bean
    @ConditionalOnProperty(prefix = "roaker.lock", name = "type", havingValue = "db")
    public JdbcLockRegistry jdbcLockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "roaker.lock", name = "type", havingValue = "zk")
    public ZookeeperLockRegistry zookeeperLockRegistry(CuratorFramework client) {
        return new ZookeeperLockRegistry(client);
    }


    @Configuration
    @ConditionalOnProperty(prefix = "roaker.lock", name = "type", havingValue = "redis")
    public static class RedisLockConfiguration {
        public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory, LockProperties lockProperties) {
            return new RedisLockRegistry(redisConnectionFactory, lockProperties.getLockPrefix(), lockProperties.getExpireTime());
        }
    }
}
