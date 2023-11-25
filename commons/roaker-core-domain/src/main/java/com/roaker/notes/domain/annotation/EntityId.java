package com.roaker.notes.domain.annotation;

import com.roaker.notes.domain.entity.AbstractDomainEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lei.rao
 * @since 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityId {
    Class<? extends AbstractDomainEntity> entityType() default AbstractDomainEntity.class;

    Type type() default Type.SELF;

    String description() default "";

    enum Type {SELF, QUOTE}
}
