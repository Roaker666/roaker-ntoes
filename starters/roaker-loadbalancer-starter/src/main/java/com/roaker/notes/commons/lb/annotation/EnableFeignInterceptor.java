package com.roaker.notes.commons.lb.annotation;

import com.roaker.notes.commons.lb.config.FeignHttpInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lei.rao
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FeignHttpInterceptorConfig.class})
public @interface EnableFeignInterceptor {
}
