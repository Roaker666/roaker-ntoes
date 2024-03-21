package com.roaker.notes.pay.converter.order;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.pay.api.core.order.dto.PayOrderCreateReqDTO;
import com.roaker.notes.pay.core.client.dto.order.PayOrderRespDTO;
import com.roaker.notes.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderExtensionDO;
import com.roaker.notes.pay.vo.order.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * 支付订单 Convert
 *
 */
@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrderRespVO convert(PayOrderDO bean);

    com.roaker.notes.pay.api.core.order.dto.PayOrderRespDTO  convert2(PayOrderDO order);

    default PayOrderDetailsRespVO convert(PayOrderDO order, PayOrderExtensionDO orderExtension, PayAppDO app) {
        PayOrderDetailsRespVO respVO = convertDetail(order);
        respVO.setExtension(convert(orderExtension));
        if (app != null) {
            respVO.setAppName(app.getName());
        }
        return respVO;
    }
    PayOrderDetailsRespVO convertDetail(PayOrderDO bean);
    PayOrderDetailsRespVO.PayOrderExtension convert(PayOrderExtensionDO bean);

    default PageResult<PayOrderPageItemRespVO> convertPage(PageResult<PayOrderDO> page, Map<Long, PayAppDO> appMap) {
        PageResult<PayOrderPageItemRespVO> result = convertPage(page);
        result.getList().forEach(order -> RoakerMapUtils.findAndThen(appMap, order.getAppId(), app -> order.setAppName(app.getName())));
        return result;
    }
    PageResult<PayOrderPageItemRespVO> convertPage(PageResult<PayOrderDO> page);

    default List<PayOrderExcelVO> convertList(List<PayOrderDO> list, Map<Long, PayAppDO> appMap) {
        return RoakerCollectionUtils.convertList(list, order -> {
            PayOrderExcelVO excelVO = convertExcel(order);
            RoakerMapUtils.findAndThen(appMap, order.getAppId(), app -> excelVO.setAppName(app.getName()));
            return excelVO;
        });
    }
    PayOrderExcelVO convertExcel(PayOrderDO bean);

    PayOrderDO convert(PayOrderCreateReqDTO bean);

    @Mapping(target = "id", ignore = true)
    PayOrderExtensionDO convert(PayOrderSubmitReqVO bean, String userIp);

    PayOrderUnifiedReqDTO convert2(PayOrderSubmitReqVO reqVO, String userIp);

    @Mapping(expression = "java(order.getPayOrderStatus().getCode())", target = "status")
    PayOrderSubmitRespVO convert(PayOrderDO order, PayOrderRespDTO respDTO);

    AppPayOrderSubmitRespVO convert3(PayOrderSubmitRespVO bean);

}
