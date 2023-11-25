package com.roaker.notes.domain.entity;

import com.roaker.notes.domain.exception.DomainDeclaredException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class AbstractDomainEntity implements DomainEntity {
    private DomainEntityId<?> innerEntityId;
    @Getter
    private AbstractDomainEntity originEntity;

    private final AtomicBoolean isDirty = new AtomicBoolean(Boolean.FALSE);

    @Override
    public DomainEntityId<?> getEntityId() {
        if (Objects.nonNull(innerEntityId)) {
            return this.innerEntityId;
        }
        synchronized (this) {
            if (Objects.isNull(innerEntityId)) {
                this.innerEntityId = resolveEntityId();
            }
        }
        return this.innerEntityId;
    }

    protected DomainEntityId<?> resolveEntityId() {
        if (!DomainEntityMetaManager.containsEntityMeta(this.getClass())) {
            DomainEntityMetaManager.registerEntity(this.getClass());
        }

        DomainEntityMeta entityMeta = DomainEntityMetaManager.getEntityMeta(this.getClass());
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass(), AbstractDomainEntity.class);
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                if (Objects.equals(descriptor.getDisplayName(), entityMeta.getEntityId().getAttrName())) {
                    return (DomainEntityId<?>) descriptor.getReadMethod().invoke(this);
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new DomainDeclaredException("class:" + this.getClass().getName() + " is not a java bean!", e);
        }
        return DefaultEntityUniqueId.build(UUID.randomUUID().toString());
    }
}
