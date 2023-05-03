package com.roaker.notes.infra.encrypt.convert;

import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileConfigDO;
import com.roaker.notes.infra.encrypt.vo.FileConfigCreateReqVO;
import com.roaker.notes.infra.encrypt.vo.FileConfigRespVO;
import com.roaker.notes.infra.encrypt.vo.FileConfigUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FileConfigConvert {

    FileConfigConvert INSTANCE = Mappers.getMapper(FileConfigConvert.class);

    @Mapping(target = "config", ignore = true)
    FileConfigDO convert(FileConfigCreateReqVO bean);

    @Mapping(target = "config", ignore = true)
    FileConfigDO convert(FileConfigUpdateReqVO bean);

    FileConfigRespVO convert(FileConfigDO bean);

    List<FileConfigRespVO> convertList(List<FileConfigDO> list);

    PageResult<FileConfigRespVO> convertPage(PageResult<FileConfigDO> page);

}