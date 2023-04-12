package com.roaker.notes.ac.converter;

import com.roaker.notes.ac.controller.admin.oauth2.vo.token.Oauth2AccessTokenRespVO;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.ac.api.oauth2.dto.Oauth2AccessTokenCheckRespDTO;
import com.roaker.notes.ac.api.oauth2.dto.Oauth2AccessTokenRespDTO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Oauth2TokenConvert {

    Oauth2TokenConvert INSTANCE = Mappers.getMapper(Oauth2TokenConvert.class);

    Oauth2AccessTokenCheckRespDTO convert(Oauth2AccessTokenDO bean);

    PageResult<Oauth2AccessTokenRespVO> convert(PageResult<Oauth2AccessTokenDO> page);

    Oauth2AccessTokenRespVO map(Oauth2AccessTokenDO value);

    Oauth2AccessTokenRespDTO convert2(Oauth2AccessTokenDO bean);

}
