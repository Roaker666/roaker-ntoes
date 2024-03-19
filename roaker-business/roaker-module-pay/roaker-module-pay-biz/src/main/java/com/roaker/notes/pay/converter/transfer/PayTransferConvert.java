package com.roaker.notes.pay.converter.transfer;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.api.core.transfer.dto.PayTransferCreateReqDTO;
import com.roaker.notes.pay.core.client.dto.transfer.PayTransferUnifiedReqDTO;
import com.roaker.notes.pay.dal.dataobject.tranfer.PayTransferDO;
import com.roaker.notes.pay.vo.tranfer.PayTransferCreateReqVO;
import com.roaker.notes.pay.vo.tranfer.PayTransferPageItemRespVO;
import com.roaker.notes.pay.vo.tranfer.PayTransferRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayTransferConvert {

    PayTransferConvert INSTANCE = Mappers.getMapper(PayTransferConvert.class);

    PayTransferDO convert(PayTransferCreateReqDTO dto);

    PayTransferUnifiedReqDTO convert2(PayTransferDO dto);

    PayTransferCreateReqDTO convert(PayTransferCreateReqVO vo);

    PayTransferRespVO convert(PayTransferDO bean);

    PageResult<PayTransferPageItemRespVO> convertPage(PageResult<PayTransferDO> pageResult);
}
