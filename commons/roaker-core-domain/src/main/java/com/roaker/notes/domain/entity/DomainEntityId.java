package com.roaker.notes.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author lei.rao
 * @since 1.0
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class DomainEntityId<T> {
    @NonNull
    private final T innerId;

    public final T getId() {
        return this.innerId;
    }
}
