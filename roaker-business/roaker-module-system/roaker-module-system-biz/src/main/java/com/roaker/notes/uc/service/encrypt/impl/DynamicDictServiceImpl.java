package com.roaker.notes.uc.service.encrypt.impl;

import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.converter.encrypt.DynamicConverter;
import com.roaker.notes.uc.dal.dataobject.encrypt.DynamicDictDO;
import com.roaker.notes.uc.dal.mapper.encrypt.DynamicDictMapper;
import com.roaker.notes.uc.dto.encrypt.DynamicDictDTO;
import com.roaker.notes.uc.service.encrypt.DynamicDictService;
import com.roaker.notes.uc.vo.encrypt.DynamicCreateReqVO;
import com.roaker.notes.uc.vo.encrypt.DynamicExportReqVO;
import com.roaker.notes.uc.vo.encrypt.DynamicPageReqVO;
import com.roaker.notes.uc.vo.encrypt.DynamicUpdateReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
}
