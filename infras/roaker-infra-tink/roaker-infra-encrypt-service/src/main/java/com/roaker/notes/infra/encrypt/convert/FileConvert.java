package com.roaker.notes.infra.encrypt.convert;

import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileDO;
import com.roaker.notes.infra.encrypt.vo.FileRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    FileRespVO convert(FileDO bean);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> page);

}
