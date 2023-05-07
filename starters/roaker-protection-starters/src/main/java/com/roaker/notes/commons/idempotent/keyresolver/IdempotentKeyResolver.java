package com.roaker.notes.commons.idempotent.keyresolver;

import com.roaker.notes.commons.idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface IdempotentKeyResolver {
    /**
     * 解析一个 Key
     *
     * @param idempotent 幂等注解
     * @param joinPoint  AOP 切面
     * @return Key
     */
    String resolver(JoinPoint joinPoint, Idempotent idempotent);
}
