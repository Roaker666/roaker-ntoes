package com.roaker.notes.domain.entity;

import com.roaker.notes.domain.annotation.EntityId;
import com.roaker.notes.domain.exception.DomainDeclaredException;
import com.roaker.notes.domain.repository.DomainRepository;
import lombok.extern.slf4j.Slf4j;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class DomainEntityMetaManager {
    private static Reflections reflections;

    private static ApplicationContext applicationContext;

    private static final Map<Class<? extends AbstractDomainEntity>, DomainEntityMeta> metaCache = new HashMap<>();

    public static void init(ApplicationContext applicationContext) {
        DomainEntityMetaManager.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public synchronized static <T extends AbstractDomainEntity> void registerEntity(Class<T> clazz) {
        List<DomainEntityMeta.DomainEntityIdMeta> quoteEntity = new LinkedList<>();
        DomainEntityMeta.DomainEntityIdMeta selfEntity = null;

        Predicate<AnnotatedElement> annotatedPredicate = ReflectionUtils.withAnnotation(EntityId.class);
        Predicate<Field> fieldPredicate = ReflectionUtils.withTypeAssignableTo(DomainEntityId.class);
        for (Field field : ReflectionUtils.getAllFields(clazz, annotatedPredicate, fieldPredicate)) {
            EntityId annotation = field.getAnnotation(EntityId.class);
            if (annotation.type() == EntityId.Type.QUOTE) {
                quoteEntity.add(new DomainEntityMeta.DomainEntityIdMeta(field, annotation.entityType()));
                continue;
            }
            if (!annotation.entityType().isAssignableFrom(clazz)) {
                String errMsg = String.format("domain entity clazz declared error: domain entity self:" +
                        " %s-%s must be super class of %s", EntityId.class.getName(), annotation.type().getClass(), clazz.getName());
                throw new DomainDeclaredException(errMsg);
            }
            selfEntity = new DomainEntityMeta.DomainEntityIdMeta(field, clazz);
        }

        if (!Objects.equals(DomainAggregateRoot.class, clazz) && Objects.isNull(selfEntity)) {
            String errMsg = String.format("domain entity %s must have one self: %s", clazz.getName(), EntityId.class.getName());
            throw new DomainDeclaredException(errMsg);
        }

        DomainEntityMeta entityMeta = DomainEntityMeta.builder().entityId(selfEntity).entityType(clazz).quoteEntityIds(quoteEntity).build();
        metaCache.put(clazz, entityMeta);
        entityMeta.setRepository(findRepository(clazz));
    }

    public static <T extends AbstractDomainEntity> void unregisterEntity(Class<T> clazz) {
        metaCache.remove(clazz);
    }

    public static <T extends AbstractDomainEntity> boolean containsEntityMeta(Class<T> clazz) {
        return metaCache.containsKey(clazz);
    }

    public static <T extends AbstractDomainEntity> DomainEntityMeta getEntityMeta(Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            throw new IllegalArgumentException("param clazz is null");
        }
        return metaCache.get(clazz);
    }

    @SuppressWarnings("unchecked")
    private static <K extends DomainEntityId<?>, V extends AbstractDomainEntity> DomainRepository<K, V> findRepository(Class<V> clazz) {
        for (Object object : applicationContext.getBeansOfType(DomainRepository.class).values()) {
            Type[] genericTypes = GenericTypeResolver.resolveTypeArguments(object.getClass(), DomainRepository.class);
            assert Objects.nonNull(genericTypes) && genericTypes.length == 2;

            if (Objects.equals(clazz, genericTypes[1])) {
                DomainEntityMeta.DomainEntityIdMeta entityMeta = getEntityMeta(clazz).getEntityId();

                if (Objects.equals(entityMeta.getType(), genericTypes[0])) {
                    return (DomainRepository<K, V>) object;
                }
                String errMsg = String.format("entity type(%s) and " + "entityId type:(%s) not match in %s",
                        genericTypes[1].getTypeName(), genericTypes[0].getTypeName(), object.getClass());
                throw new DomainDeclaredException(errMsg);
            }
        }
        return key -> null;
    }
}
