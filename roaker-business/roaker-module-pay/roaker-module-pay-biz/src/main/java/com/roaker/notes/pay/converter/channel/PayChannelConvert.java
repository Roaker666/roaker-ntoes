package com.roaker.notes.pay.converter.channel;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.dal.dataobject.channel.PayChannelDO;
import com.roaker.notes.pay.vo.channel.PayChannelCreateReqVO;
import com.roaker.notes.pay.vo.channel.PayChannelRespVO;
import com.roaker.notes.pay.vo.channel.PayChannelUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayChannelConvert {

    PayChannelConvert INSTANCE = Mappers.getMapper(PayChannelConvert.class);

    @Mapping(target = "config",ignore = true)
    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    PayChannelDO convert(PayChannelCreateReqVO bean);

    @Mapping(target = "config",ignore = true)
    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    PayChannelDO convert(PayChannelUpdateReqVO bean);

    @Mapping(target = "config",expression = "java(com.roaker.notes.commons.utils.JacksonUtils.toJSON(bean.getConfig()))")
    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    PayChannelRespVO convert(PayChannelDO bean);

    PageResult<PayChannelRespVO> convertPage(PageResult<PayChannelDO> page);

}
