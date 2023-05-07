package com.roaker.notes.commons.idempotent.annotation;

import com.roaker.notes.commons.idempotent.keyresolver.IdempotentKeyResolver;
import com.roaker.notes.commons.idempotent.keyresolver.impl.DefaultIdempotentKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author lei.rao
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * 幂等的超时时间，默认为 60 秒
     *
     * 注意，如果执行时间超过它，请求还是会进来
     */
    int timeout() default 60;
    /**
     * 时间单位，默认为 SECONDS 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    /**
     * 提示信息，正在执行中的提示
     */
    String message() default "重复请求,请稍后重试";
    /**
     * 使用的 Key 解析器
     */
    Class<? extends IdempotentKeyResolver> keyResolver() default DefaultIdempotentKeyResolver.class;
    /**
     * 使用的 Key 参数
     */
    String keyArg() default "";

}
