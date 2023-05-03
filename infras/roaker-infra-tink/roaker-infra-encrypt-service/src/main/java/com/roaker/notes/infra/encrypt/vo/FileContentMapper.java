package com.roaker.notes.infra.encrypt.vo;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileContentDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileContentMapper extends BaseMapperX<FileContentDO> {

}