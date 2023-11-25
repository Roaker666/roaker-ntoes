package com.roaker.notes.domain.entity;

import lombok.NonNull;

/**
 * @author lei.rao
 * @since 1.0
 */
public class DefaultEntityUniqueId extends DomainEntityId<String> {
    public DefaultEntityUniqueId(@NonNull String innerId) {
        super(innerId);
    }

    public static DefaultEntityUniqueId build(@NonNull String innerId) {
        return new DefaultEntityUniqueId(innerId);
    }
}
