package com.roaker.notes.infra.encrypt.convert;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.infra.encrypt.dal.dataobject.DynamicDictDO;
import com.roaker.notes.infra.encrypt.dto.DynamicDictDTO;
import com.roaker.notes.infra.encrypt.vo.DynamicCreateReqVO;
import com.roaker.notes.infra.encrypt.vo.DynamicExportRespVO;
import com.roaker.notes.infra.encrypt.vo.DynamicUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface DynamicConverter {
    DynamicConverter INSTANCE = Mappers.getMapper(DynamicConverter.class);

    DynamicDictDTO convert(DynamicDictDO dictDO);

    @Mapping(target = "type", expression = "java(com.roaker.notes.dynamic.enums.DynamicDictTypeEnums.codeOf(reqVO.getType()))")
    DynamicDictDO convert(DynamicCreateReqVO reqVO);

    @Mapping(target = "type", expression = "java(com.roaker.notes.dynamic.enums.DynamicDictTypeEnums.codeOf(reqVO.getType()))")
    DynamicDictDO convert(DynamicUpdateReqVO reqVO);

    List<DynamicDictDTO> convertList01(List<DynamicDictDO> dictDO);

    List<DynamicExportRespVO> convertList02(List<DynamicDictDO> dictDO);

    PageResult<DynamicDictDTO> convertPage(PageResult<DynamicDictDO> page);
}
