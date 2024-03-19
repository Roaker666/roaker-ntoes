package com.roaker.notes.uc.api.dict;


import com.roaker.notes.dynamic.enums.DynamicDictDO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface DynamicApi {

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型{@link com.roaker.notes.dynamic.enums.DynamicDictTypeEnums}
     * @param values   字典数据值的数组
     */
    void validateDictDataList(String dictType, Collection<String> values);

    /**
     * 获得指定的字典数据，从缓存中
     *
     * @param type  字典类型
     * @param value 字典数据值
     * @return 字典数据
     */
    DynamicDictDO getDictData(String type, String value);

    /**
     * 获得指定字典类型的字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<DynamicDictDO> getDictDataList(String dictType);


    /**
     * 解析获得指定的字典数据，从缓存中
     *
     * @param type  字典类型
     * @param label 字典数据标签
     * @return 字典数据
     */
    DynamicDictDO parseDictData(String type, String label);

    /**
     * 获得最近更新的的字典数据列表
     *
     * @param updateDate 更新时间
     * @return 字典数据列表
     */
    List<DynamicDictDO> loadDataList(LocalDateTime updateDate);
}
