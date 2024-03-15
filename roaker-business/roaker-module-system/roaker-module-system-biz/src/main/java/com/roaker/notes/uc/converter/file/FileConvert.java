package com.roaker.notes.uc.converter.file;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.dal.dataobject.file.FileDO;
import com.roaker.notes.uc.vo.file.FileRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    FileRespVO convert(FileDO bean);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> page);

}
