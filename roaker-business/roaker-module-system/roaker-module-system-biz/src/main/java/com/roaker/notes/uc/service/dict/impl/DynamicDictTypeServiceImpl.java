package com.roaker.notes.uc.service.dict.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.annotations.VisibleForTesting;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.BeanUtils;
import com.roaker.notes.commons.utils.date.LocalDateTimeUtils;
import com.roaker.notes.uc.dal.dataobject.dict.DynamicDictTypeDO;
import com.roaker.notes.uc.dal.mapper.dict.DynamicDictTypeMapper;
import com.roaker.notes.uc.service.dict.DynamicDictService;
import com.roaker.notes.uc.service.dict.DynamicDictTypeService;
import com.roaker.notes.uc.vo.dict.type.DictTypePageReqVO;
import com.roaker.notes.uc.vo.dict.type.DictTypeSaveReqVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.*;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * 字典类型 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class DynamicDictTypeServiceImpl implements DynamicDictTypeService {

    @Resource
    private DynamicDictService dynamicDictService;

    @Resource
    private DynamicDictTypeMapper dynamicDictTypeMapper;

    @Override
    public PageResult<DynamicDictTypeDO> getDictTypePage(DictTypePageReqVO pageReqVO) {
        return dynamicDictTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public DynamicDictTypeDO getDictType(Long id) {
        return dynamicDictTypeMapper.selectById(id);
    }

    @Override
    public DynamicDictTypeDO getDictType(String type) {
        return dynamicDictTypeMapper.selectByType(type);
    }

    @Override
    public Long createDictType(DictTypeSaveReqVO createReqVO) {
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(null, createReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(null, createReqVO.getType());

        // 插入字典类型
        DynamicDictTypeDO dictType = BeanUtils.toBean(createReqVO, DynamicDictTypeDO.class);
        dictType.setDeletedTime(LocalDateTimeUtils.EMPTY); // 唯一索引，避免 null 值
        dynamicDictTypeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void updateDictType(DictTypeSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictTypeExists(updateReqVO.getId());
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(updateReqVO.getId(), updateReqVO.getType());

        // 更新字典类型
        DynamicDictTypeDO updateObj = BeanUtils.toBean(updateReqVO, DynamicDictTypeDO.class);
        dynamicDictTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictType(Long id) {
        // 校验是否存在
        DynamicDictTypeDO dictType = validateDictTypeExists(id);
        // 校验是否有字典数据
        if (dynamicDictService.getDictDataCountByDictType(dictType.getDictType()) > 0) {
            throw exception(DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        dynamicDictTypeMapper.updateToDelete(id, LocalDateTime.now());
    }

    @Override
    public List<DynamicDictTypeDO> getDictTypeList() {
        return dynamicDictTypeMapper.selectList();
    }

    @VisibleForTesting
    void validateDictTypeNameUnique(Long id, String name) {
        DynamicDictTypeDO dictType = dynamicDictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
    }

    @VisibleForTesting
    void validateDictTypeUnique(Long id, String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        DynamicDictTypeDO dictType = dynamicDictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    @VisibleForTesting
    DynamicDictTypeDO validateDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        DynamicDictTypeDO dictType = dynamicDictTypeMapper.selectById(id);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

}
