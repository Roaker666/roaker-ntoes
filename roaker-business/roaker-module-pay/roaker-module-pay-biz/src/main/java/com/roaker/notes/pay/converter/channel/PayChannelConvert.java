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
    PayChannelDO convert(PayChannelCreateReqVO bean);

    @Mapping(target = "config",ignore = true)
    PayChannelDO convert(PayChannelUpdateReqVO bean);

    @Mapping(target = "config",expression = "java(com.roaker.notes.commons.utils.JacksonUtils.toJSON(bean.getConfig()))")
    PayChannelRespVO convert(PayChannelDO bean);

    PageResult<PayChannelRespVO> convertPage(PageResult<PayChannelDO> page);

}
