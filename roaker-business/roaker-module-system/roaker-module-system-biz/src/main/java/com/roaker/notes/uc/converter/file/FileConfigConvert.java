package com.roaker.notes.uc.converter.file;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.dal.dataobject.file.FileConfigDO;
import com.roaker.notes.uc.vo.file.FileConfigCreateReqVO;
import com.roaker.notes.uc.vo.file.FileConfigRespVO;
import com.roaker.notes.uc.vo.file.FileConfigUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FileConfigConvert {

    FileConfigConvert INSTANCE = Mappers.getMapper(FileConfigConvert.class);

    @Mapping(target = "config", ignore = true)
    @Mapping(target = "storage", expression = "java(com.roaker.notes.file.core.enums.FileStorageEnum.getByStorage(bean.getStorage()))")
    FileConfigDO convert(FileConfigCreateReqVO bean);

    @Mapping(target = "config", ignore = true)
    FileConfigDO convert(FileConfigUpdateReqVO bean);
    @Mapping(target = "storage", expression = "java(bean.getStorage().getStorage())")
    FileConfigRespVO convert(FileConfigDO bean);

    List<FileConfigRespVO> convertList(List<FileConfigDO> list);

    PageResult<FileConfigRespVO> convertPage(PageResult<FileConfigDO> page);

}