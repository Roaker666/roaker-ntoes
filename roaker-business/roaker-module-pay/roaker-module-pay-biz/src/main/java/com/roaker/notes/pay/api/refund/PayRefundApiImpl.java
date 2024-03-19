package com.roaker.notes.pay.api.refund;

import com.roaker.notes.pay.api.core.refund.PayRefundApi;
import com.roaker.notes.pay.api.core.refund.dto.PayRefundCreateReqDTO;
import com.roaker.notes.pay.api.core.refund.dto.PayRefundRespDTO;
import com.roaker.notes.pay.converter.refund.PayRefundConvert;
import com.roaker.notes.pay.service.refund.PayRefundService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 退款单 API 实现类
 *
 */
@Service
@Validated
public class PayRefundApiImpl implements PayRefundApi {

    @Resource
    private PayRefundService payRefundService;

    @Override
    public Long createRefund(PayRefundCreateReqDTO reqDTO) {
        return payRefundService.createPayRefund(reqDTO);
    }

    @Override
    public PayRefundRespDTO getRefund(Long id) {
        return PayRefundConvert.INSTANCE.convert02(payRefundService.getRefund(id));
    }

}
