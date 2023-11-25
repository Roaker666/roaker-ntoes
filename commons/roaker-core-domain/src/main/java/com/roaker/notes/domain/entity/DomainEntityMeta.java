package com.roaker.notes.domain.entity;

import com.roaker.notes.domain.repository.DomainRepository;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author lei.rao
 * @since 1.0
 */
@Getter
@Builder
public class DomainEntityMeta {
    //实体类型
    private Class<? extends AbstractDomainEntity> entityType;
    //实体的Id id type
    private DomainEntityIdMeta entityId;
    //关联的实体Id
    private List<DomainEntityIdMeta> quoteEntityIds;
    //关联的repository
    @Setter
    private DomainRepository repository;

    public boolean isAggregateRoot() {
        return DomainAggregateRoot.class.isAssignableFrom(entityType);
    }

    @Data
    public static class DomainEntityIdMeta {
        private Field entityIdField;

        private Class<? extends AbstractDomainEntity> entityType;

        public DomainEntityIdMeta(Field entityIdField, Class<? extends AbstractDomainEntity> entityType) {
            assert Objects.nonNull(entityIdField) && Objects.nonNull(entityType);
            assert DomainEntityId.class.isAssignableFrom(entityIdField.getType());

            this.entityIdField = entityIdField;
            this.entityType = entityType;
        }

        @SuppressWarnings("unchecked")
        public Class<DomainEntityId<?>> getType() {
            return (Class<DomainEntityId<?>>) entityIdField.getType();
        }

        public String getAttrName() {
            return entityIdField.getName();
        }
    }
}