package com.roaker.notes.pay.api.transfer;

import com.roaker.notes.pay.api.core.transfer.PayTransferApi;
import com.roaker.notes.pay.api.core.transfer.dto.PayTransferCreateReqDTO;
import com.roaker.notes.pay.service.tranfer.PayTransferService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 * 转账单 API 实现类
 *
 */
@Service
@Validated
public class PayTransferApiImpl implements PayTransferApi {

    @Resource
    private PayTransferService payTransferService;

    @Override
    public Long createTransfer(PayTransferCreateReqDTO reqDTO) {
        return payTransferService.createTransfer(reqDTO);
    }

}
