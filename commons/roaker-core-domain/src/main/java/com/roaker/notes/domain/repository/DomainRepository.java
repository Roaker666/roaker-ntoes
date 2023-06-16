package com.roaker.notes.domain.repository;

import com.roaker.notes.domain.entity.AbstractDomainEntity;
import com.roaker.notes.domain.entity.DomainEntityId;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface DomainRepository<K extends DomainEntityId<?>, V extends AbstractDomainEntity> extends Repository<K, V> {
}

