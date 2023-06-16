package com.roaker.notes.domain.entity;

import com.roaker.notes.domain.exception.DomainDeclaredException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
public class DomainAggregateRoot extends AbstractDomainEntity {
    private Map<DomainEntityId<?>, Class<? extends AbstractDomainEntity>> entities;

    public Map<DomainEntityId<?>, Class<? extends AbstractDomainEntity>> getAllEntityId() {
        if (Objects.isNull(entities)) {
            this.doResolveEntityId();
        }
        return entities;
    }

    private void doResolveEntityId() {
        if (!DomainEntityMetaManager.containsEntityMeta(this.getClass())) {
            DomainEntityMetaManager.registerEntity(this.getClass());
        }
        DomainEntityMeta entityMeta = DomainEntityMetaManager.getEntityMeta(this.getClass());
        Map<String, DomainEntityMeta.DomainEntityIdMeta> fieldMetas = entityMeta.getQuoteEntityIds().stream().collect(
                Collectors.toMap(DomainEntityMeta.DomainEntityIdMeta::getAttrName, Function.identity()));
        this.entities = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass(), AbstractDomainEntity.class);
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                DomainEntityMeta.DomainEntityIdMeta entityIdMeta = fieldMetas.get(descriptor.getDisplayName());
                if (Objects.isNull(entityIdMeta)) {
                    continue;
                }
                DomainEntityId<?> value = (DomainEntityId<?>)descriptor.getReadMethod().invoke(this);
                if (Objects.nonNull(value)) {
                    entities.put(value, entityIdMeta.getEntityType());
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new DomainDeclaredException("class:" + this.getClass().getName() + " is not a java bean!", e);
        }
    }
}

