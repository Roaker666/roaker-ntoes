package com.roaker.notes.domain.entity;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface DomainEntity extends Serializable {
    /**
     * 领域实体ID
     * @return 实体
     */
    DomainEntityId<?> getEntityId();

    /**
     * 保存领域实体
     * @return 操作结果
     */
    default boolean save() {
        return true;
    }

    /**
     * 废弃/丢弃领域实体
     * @return 操作结果
     */
    default boolean discard() {
        return true;
    }
}

