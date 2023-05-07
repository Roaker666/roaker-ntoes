package com.roaker.notes.commons.idempotent.redis;

import com.roaker.notes.commons.redis.core.RedisKeyDefine;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
public class IdempotentRedisDAO {
    private static final RedisKeyDefine IDEMPOTENT = new RedisKeyDefine(
            "幂等操作",
            "idempotent:%s",
            RedisKeyDefine.KeyTypeEnum.STRING,
            String.class,
            RedisKeyDefine.TimeoutTypeEnum.DYNAMIC);

    private final RedisTemplate<String, Object> redisTemplate;

    public Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
        String redisKey = formatKey(key);
        return redisTemplate.opsForValue().setIfAbsent(redisKey, "", timeout, timeUnit);
    }


    public Object get(String key) {
        String redisKey = formatKey(key);
        return redisTemplate.opsForValue().get(key);
    }

    private static String formatKey(String key) {
        return String.format(IDEMPOTENT.getKeyTemplate(), key);
    }

}
