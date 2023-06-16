package com.roaker.notes.domain.repository;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface Repository<K, V> {
    /**
     * 执行loader操作
     *
     * @param key 对实体的Id
     * @return 实体
     */
    V load(K key);

    /**
     * 执行create操作
     *
     * @param entity 实体
     * @return 操作结果
     */
    default boolean save(V entity) {
        return true;
    }

    /**
     * 执行update操作
     *
     * @param oldEntity 原始实体
     * @param newEntity 新实体
     * @return 操作结果
     */
    default boolean update(V oldEntity, V newEntity) {
        return true;
    }

    /**
     * 执行delete操作
     *
     * @param entity 实体
     * @return 操作结果
     */
    default boolean delete(V entity) {
        return true;
    }
}
