package com.roaker.notes.commons.db.core.annotation;

import java.lang.annotation.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BizKey {
    String bizName() default "DEFAULT";

    String bizPrefix() default "";
}
