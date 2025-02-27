package com.roaker.notes.commons.log.annotation;

import java.lang.annotation.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    /**
     * 操作信息
     */
    String operation();
}