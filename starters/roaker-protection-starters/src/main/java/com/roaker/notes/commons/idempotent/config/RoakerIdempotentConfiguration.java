package com.roaker.notes.commons.idempotent.config;

import com.roaker.notes.commons.idempotent.aop.IdempotentAspect;
import com.roaker.notes.commons.idempotent.keyresolver.IdempotentKeyResolver;
import com.roaker.notes.commons.idempotent.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.roaker.notes.commons.idempotent.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.roaker.notes.commons.idempotent.redis.IdempotentRedisDAO;
import com.roaker.notes.commons.redis.config.RoakerCacheAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@AutoConfigureAfter(RoakerCacheAutoConfiguration.class)
@Configuration
public class RoakerIdempotentConfiguration {
    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }
}
