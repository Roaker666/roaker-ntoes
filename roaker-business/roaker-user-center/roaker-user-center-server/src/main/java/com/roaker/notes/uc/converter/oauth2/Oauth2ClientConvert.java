package com.roaker.notes.uc.converter.oauth2;

import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientCreateReqVO;
import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientRespVO;
import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientUpdateReqVO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Oauth2 客户端 Convert
 *
 * @author lei.rao
 */
@Mapper
public interface Oauth2ClientConvert {
    Oauth2ClientConvert INSTANCE = Mappers.getMapper(Oauth2ClientConvert.class);

    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    Oauth2ClientDO convert(Oauth2ClientCreateReqVO bean);

    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    Oauth2ClientDO convert(Oauth2ClientUpdateReqVO bean);
    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    Oauth2ClientRespVO convert(Oauth2ClientDO bean);

    List<Oauth2ClientRespVO> convertList(List<Oauth2ClientDO> list);

    PageResult<Oauth2ClientRespVO> convertPage(PageResult<Oauth2ClientDO> page);

}
