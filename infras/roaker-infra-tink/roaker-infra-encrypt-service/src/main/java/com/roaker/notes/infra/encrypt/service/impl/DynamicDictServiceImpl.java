package com.roaker.notes.infra.encrypt.service.impl;

import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.infra.encrypt.convert.DynamicConverter;
import com.roaker.notes.infra.encrypt.dal.dataobject.DynamicDictDO;
import com.roaker.notes.infra.encrypt.dal.mapper.DynamicDictMapper;
import com.roaker.notes.infra.encrypt.dto.DynamicDictDTO;
import com.roaker.notes.infra.encrypt.service.DynamicDictService;
import com.roaker.notes.infra.encrypt.vo.DynamicCreateReqVO;
import com.roaker.notes.infra.encrypt.vo.DynamicExportReqVO;
import com.roaker.notes.infra.encrypt.vo.DynamicPageReqVO;
import com.roaker.notes.infra.encrypt.vo.DynamicUpdateReqVO;
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
