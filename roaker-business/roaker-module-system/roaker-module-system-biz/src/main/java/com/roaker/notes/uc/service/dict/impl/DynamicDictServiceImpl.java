package com.roaker.notes.uc.service.dict.impl;

import cn.hutool.core.collection.CollUtil;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.converter.encrypt.DynamicConverter;
import com.roaker.notes.uc.dal.dataobject.dict.DynamicDictDO;
import com.roaker.notes.uc.dal.mapper.dict.DynamicDictMapper;
import com.roaker.notes.uc.dto.encrypt.DynamicDictDTO;
import com.roaker.notes.uc.service.dict.DynamicDictService;
import com.roaker.notes.uc.vo.dict.data.DynamicCreateReqVO;
import com.roaker.notes.uc.vo.dict.data.DynamicExportReqVO;
import com.roaker.notes.uc.vo.dict.data.DynamicPageReqVO;
import com.roaker.notes.uc.vo.dict.data.DynamicUpdateReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.DICT_DATA_NOT_ENABLE;
import static com.roaker.notes.commons.constants.ErrorCodeConstants.DICT_DATA_NOT_EXISTS;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicDictServiceImpl implements DynamicDictService {
    private final DynamicDictMapper dynamicDictMapper;

    @Override
    public List<DynamicDictDTO> getDynamicDictList(LocalDateTime minUpdateTime) {
        return DynamicConverter.INSTANCE.convertList01(dynamicDictMapper.selectList(new LambdaQueryWrapperX<DynamicDictDO>()
                .ge(BaseDO::getUpdateTime, minUpdateTime)
                .orderByAsc(DynamicDictDO::getId)));
    }

    @Override
    public Long createDynamicDict(DynamicCreateReqVO createReqVO) {
        DynamicDictDO dictDO = DynamicConverter.INSTANCE.convert(createReqVO);
        dynamicDictMapper.insert(dictDO);
        return dictDO.getId();
    }

    @Override
    public void updateDynamicDict(DynamicUpdateReqVO updateReqVO) {
        DynamicDictDO dictDO = DynamicConverter.INSTANCE.convert(updateReqVO);
        dynamicDictMapper.updateById(dictDO);
    }

    @Override
    public void deleteDynamicDict(Long id) {
        dynamicDictMapper.deleteById(id);
    }

    @Override
    public DynamicDictDO getDynamicDict(Long id) {
        return dynamicDictMapper.selectById(id);
    }

    @Override
    public PageResult<DynamicDictDO> getDynamicDictPage(DynamicPageReqVO pageReqVO) {
        return dynamicDictMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DynamicDictDO> getDynamicDictList(DynamicExportReqVO exportReqVO) {
        return dynamicDictMapper.selectList(exportReqVO);
    }

    @Override
    public long getDictDataCountByDictType(String dictType) {
        return dynamicDictMapper.selectCountByDictType(dictType);
    }

    @Override
    public List<DynamicDictDO> getDictDataList(Integer status, String dictType) {
        List<DynamicDictDO> list = dynamicDictMapper.selectList(DynamicDictDO::getType, dictType);
        list.sort(Comparator.comparing(DynamicDictDO::getSort));
        return list;
    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        Map<String, DynamicDictDO> dictDataMap = RoakerCollectionUtils.convertMap(
                dynamicDictMapper.selectByDictTypeAndValues(dictType, values), DynamicDictDO::getValue);
        // 校验
        values.forEach(value -> {
            DynamicDictDO dictData = dictDataMap.get(value);
            if (dictData == null) {
                throw exception(DICT_DATA_NOT_EXISTS);
            }
            if (CommonStatusEnum.ENABLE != dictData.getStatus()) {
                throw exception(DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        });
    }

    @Override
    public DynamicDictDO getDictData(String dictType, String value) {
        return dynamicDictMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public DynamicDictDO parseDictData(String dictType, String label) {
        return dynamicDictMapper.selectByDictTypeAndLabel(dictType, label);
    }

    @Override
    public List<DynamicDictDO> getDictDataListByDictType(String dictType) {
        List<DynamicDictDO> list = dynamicDictMapper.selectList(DynamicDictDO::getType, dictType);
        list.sort(Comparator.comparing(DynamicDictDO::getSort));
        return list;
    }


}
