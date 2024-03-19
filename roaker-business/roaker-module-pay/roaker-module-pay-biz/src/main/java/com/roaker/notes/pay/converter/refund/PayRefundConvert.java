package com.roaker.notes.pay.converter.refund;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.pay.api.core.refund.dto.PayRefundCreateReqDTO;
import com.roaker.notes.pay.api.core.refund.dto.PayRefundRespDTO;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.dal.dataobject.refund.PayRefundDO;
import com.roaker.notes.pay.vo.refund.PayRefundDetailsRespVO;
import com.roaker.notes.pay.vo.refund.PayRefundExcelVO;
import com.roaker.notes.pay.vo.refund.PayRefundPageItemRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayRefundConvert {

    PayRefundConvert INSTANCE = Mappers.getMapper(PayRefundConvert.class);


    default PayRefundDetailsRespVO convert(PayRefundDO refund, PayAppDO app) {
        PayRefundDetailsRespVO respVO = convert(refund);
        if (app != null) {
            respVO.setAppName(app.getName());
        }
        return respVO;
    }
    PayRefundDetailsRespVO convert(PayRefundDO bean);
    PayRefundDetailsRespVO.Order convert(PayOrderDO bean);

    default PageResult<PayRefundPageItemRespVO> convertPage(PageResult<PayRefundDO> page, Map<Long, PayAppDO> appMap) {
        PageResult<PayRefundPageItemRespVO> result = convertPage(page);
        result.getList().forEach(order -> RoakerMapUtils.findAndThen(appMap, order.getAppId(), app -> order.setAppName(app.getName())));
        return result;
    }
    PageResult<PayRefundPageItemRespVO> convertPage(PageResult<PayRefundDO> page);

    PayRefundDO convert(PayRefundCreateReqDTO bean);

    PayRefundRespDTO convert02(PayRefundDO bean);

    default List<PayRefundExcelVO> convertList(List<PayRefundDO> list, Map<Long, PayAppDO> appMap) {
        return RoakerCollectionUtils.convertList(list, order -> {
            PayRefundExcelVO excelVO = convertExcel(order);
            RoakerMapUtils.findAndThen(appMap, order.getAppId(), app -> excelVO.setAppName(app.getName()));
            return excelVO;
        });
    }
    PayRefundExcelVO convertExcel(PayRefundDO bean);

}
