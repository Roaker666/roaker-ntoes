package com.roaker.notes.infra.encrypt.convert;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.infra.encrypt.vo.FileRespVO;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    FileRespVO convert(FileDO bean);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> page);

}
