package com.roaker.notes.uc.api.dict;

import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.uc.dal.dataobject.dict.DynamicDictDO;
import com.roaker.notes.uc.dto.encrypt.DynamicDictDTO;
import com.roaker.notes.uc.service.dict.DynamicDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicApiImpl implements DynamicApi {
    private final DynamicDictService dynamicDictService;

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        dynamicDictService.validateDictDataList(dictType, values);
    }

    @Override
    public com.roaker.notes.dynamic.enums.DynamicDictDO getDictData(String type, String value) {
        DynamicDictDO dictData = dynamicDictService.getDictData(type, value);
        return BeanUtils.toBean(dictData, com.roaker.notes.dynamic.enums.DynamicDictDO.class);
    }

    /**
     * 解析获得指定的字典数据，从缓存中
     *
     * @param type  字典类型
     * @param label 字典数据标签
     * @return 字典数据
     */
    @Override
    public com.roaker.notes.dynamic.enums.DynamicDictDO parseDictData(String type, String label) {
        DynamicDictDO dictData = dynamicDictService.parseDictData(type, label);
        return BeanUtils.toBean(dictData, com.roaker.notes.dynamic.enums.DynamicDictDO.class);
    }

    /**
     * 获得指定字典类型的字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    @Override
    public List<com.roaker.notes.dynamic.enums.DynamicDictDO> getDictDataList(String dictType) {
        List<DynamicDictDO> dictDataListByDictType = dynamicDictService.getDictDataListByDictType(dictType);
        return BeanUtils.toBean(dictDataListByDictType, com.roaker.notes.dynamic.enums.DynamicDictDO.class);
    }

    @Override
    public List<com.roaker.notes.dynamic.enums.DynamicDictDO> loadDataList(LocalDateTime updateDate) {
        List<DynamicDictDTO> dynamicDictList = dynamicDictService.getDynamicDictList(updateDate);
        return BeanUtils.toBean(dynamicDictList, com.roaker.notes.dynamic.enums.DynamicDictDO.class);
    }
}
