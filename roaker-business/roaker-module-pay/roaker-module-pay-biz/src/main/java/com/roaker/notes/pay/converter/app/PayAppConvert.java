package com.roaker.notes.pay.converter.app;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.channel.PayChannelDO;
import com.roaker.notes.pay.vo.app.PayAppCreateReqVO;
import com.roaker.notes.pay.vo.app.PayAppPageItemRespVO;
import com.roaker.notes.pay.vo.app.PayAppRespVO;
import com.roaker.notes.pay.vo.app.PayAppUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 支付应用信息 Convert
 *
 */
@Mapper
public interface PayAppConvert {

    PayAppConvert INSTANCE = Mappers.getMapper(PayAppConvert.class);

    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    PayAppPageItemRespVO pageConvert (PayAppDO bean);
    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    PayAppDO convert(PayAppCreateReqVO bean);
    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    PayAppDO convert(PayAppUpdateReqVO bean);

    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    PayAppRespVO convert(PayAppDO bean);

    List<PayAppRespVO> convertList(List<PayAppDO> list);

    PageResult<PayAppPageItemRespVO> convertPage(PageResult<PayAppDO> page);

    default PageResult<PayAppPageItemRespVO> convertPage(PageResult<PayAppDO> pageResult, List<PayChannelDO> channels) {
        PageResult<PayAppPageItemRespVO> voPageResult = convertPage(pageResult);
        // 处理 channel 关系
        Map<Long, Set<String>> appIdChannelMap = RoakerCollectionUtils.convertMultiMap2(channels, PayChannelDO::getAppId, PayChannelDO::getCode);
        voPageResult.getList().forEach(app -> app.setChannelCodes(appIdChannelMap.get(app.getId())));
        return voPageResult;
    }

}
