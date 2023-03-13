package com.roaker.notes.ac.dal.mapper.oauth2;

import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2CodeDO;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Oauth2CodeMapper extends BaseMapperX<Oauth2CodeDO> {

    default Oauth2CodeDO selectByCode(String code) {
        return selectOne(Oauth2CodeDO::getCode, code);
    }

}
