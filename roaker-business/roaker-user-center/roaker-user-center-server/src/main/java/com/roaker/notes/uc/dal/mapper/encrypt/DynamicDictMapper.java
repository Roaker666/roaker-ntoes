package com.roaker.notes.uc.dal.mapper.encrypt;

import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.dal.dataobject.encrypt.DynamicDictDO;
import com.roaker.notes.uc.vo.encrypt.DynamicExportReqVO;
import com.roaker.notes.uc.vo.encrypt.DynamicPageReqVO;
import org.apache.ibatis.annotations.Mapper;

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
                .eqIfPresent(DynamicDictDO::getOriginCode, pageReqVO.getOriginCode())
                .orderByDesc(BaseDO::getUpdateTime)
        );
    }

    default List<DynamicDictDO> selectList(DynamicExportReqVO exportReqVO) {
        return selectList(new LambdaQueryWrapperX<DynamicDictDO>()
                .eqIfPresent(DynamicDictDO::getType, exportReqVO.getType())
                .eqIfPresent(DynamicDictDO::getBizKey, exportReqVO.getBizKey())
                .eqIfPresent(DynamicDictDO::getCode, exportReqVO.getCode())
                .eqIfPresent(DynamicDictDO::getOriginCode, exportReqVO.getOriginCode())
                .orderByDesc(BaseDO::getUpdateTime)
        );
    }
}
