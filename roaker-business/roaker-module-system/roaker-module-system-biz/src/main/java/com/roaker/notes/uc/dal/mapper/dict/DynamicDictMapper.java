package com.roaker.notes.uc.dal.mapper.dict;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import com.roaker.notes.uc.dal.dataobject.dict.DynamicDictDO;
import com.roaker.notes.uc.vo.dict.data.DynamicExportReqVO;
import com.roaker.notes.uc.vo.dict.data.DynamicPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface DynamicDictMapper extends BaseMapperX<DynamicDictDO> {
    default PageResult<DynamicDictDO> selectPage(DynamicPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<DynamicDictDO>()
                .eqIfPresent(DynamicDictDO::getType, pageReqVO.getType())
                .eqIfPresent(DynamicDictDO::getBizKey, pageReqVO.getBizKey())
                .eqIfPresent(DynamicDictDO::getCode, pageReqVO.getCode())
                .eqIfPresent(DynamicDictDO::getLabel, pageReqVO.getLabel())
                .orderByDesc(BaseDO::getUpdateTime)
        );
    }

    default List<DynamicDictDO> selectList(DynamicExportReqVO exportReqVO) {
        return selectList(new LambdaQueryWrapperX<DynamicDictDO>()
                .eqIfPresent(DynamicDictDO::getType, exportReqVO.getType())
                .eqIfPresent(DynamicDictDO::getBizKey, exportReqVO.getBizKey())
                .eqIfPresent(DynamicDictDO::getCode, exportReqVO.getCode())
                .eqIfPresent(DynamicDictDO::getLabel, exportReqVO.getLabel())
                .orderByDesc(BaseDO::getUpdateTime)
        );
    }

    default List<DynamicDictDO> selectByDictTypeAndValues(String dictType, Collection<String> values) {
        return selectList(new LambdaQueryWrapper<DynamicDictDO>().eq(DynamicDictDO::getType, dictType)
                .in(DynamicDictDO::getValue, values));
    }

    default long selectCountByDictType(String dictType) {
        return selectCount(DynamicDictDO::getType, dictType);
    }

    default DynamicDictDO selectByDictTypeAndValue(String dictType, String value) {
        return selectOne(DynamicDictDO::getType, dictType, DynamicDictDO::getValue, value);
    }

    default DynamicDictDO selectByDictTypeAndLabel(String dictType, String label) {
        return selectOne(DynamicDictDO::getType, dictType, DynamicDictDO::getLabel, label);
    }
}
