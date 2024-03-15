package com.roaker.notes.uc.dal.mapper.logger;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.api.logger.dto.OperateLogV2PageReqDTO;
import com.roaker.notes.uc.dal.dataobject.logger.OperateLogV2DO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogV2Mapper extends BaseMapperX<OperateLogV2DO> {

    default PageResult<OperateLogV2DO> selectPage(OperateLogV2PageReqDTO pageReqDTO) {
        return selectPage(pageReqDTO, new LambdaQueryWrapperX<OperateLogV2DO>()
                .eqIfPresent(OperateLogV2DO::getType, pageReqDTO.getBizType())
                .eqIfPresent(OperateLogV2DO::getBizId, pageReqDTO.getBizId())
                .eqIfPresent(OperateLogV2DO::getUserId, pageReqDTO.getUserId())
                .orderByDesc(OperateLogV2DO::getId));
    }

}
