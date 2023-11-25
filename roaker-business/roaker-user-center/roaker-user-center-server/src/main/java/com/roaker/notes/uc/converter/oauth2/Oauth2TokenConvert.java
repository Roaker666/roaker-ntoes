package com.roaker.notes.uc.converter.oauth2;

import com.roaker.notes.uc.controller.oauth2.admin.vo.token.Oauth2AccessTokenRespVO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenCheckRespDTO;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Oauth2TokenConvert {
    Oauth2TokenConvert INSTANCE = Mappers.getMapper(Oauth2TokenConvert.class);

    @Mapping(target = "userType", expression = "java(bean.getUserType().getCode())")
    Oauth2AccessTokenCheckRespDTO convert(Oauth2AccessTokenDO bean);

    PageResult<Oauth2AccessTokenRespVO> convert(PageResult<Oauth2AccessTokenDO> page);

    @Mapping(target = "userType", expression = "java(value.getUserType().getCode())")
    Oauth2AccessTokenRespVO map(Oauth2AccessTokenDO value);

    @Mapping(target = "userType", expression = "java(bean.getUserType().getCode())")
    Oauth2AccessTokenRespDTO convert2(Oauth2AccessTokenDO bean);

}
