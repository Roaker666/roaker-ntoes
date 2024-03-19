package com.roaker.notes.pay.api.core.transfer;


import com.roaker.notes.pay.api.core.transfer.dto.PayTransferCreateReqDTO;
import jakarta.validation.Valid;

/**
 * 转账单 API 接口
 *
 */
public interface PayTransferApi {

    /**
     * 创建转账单
     *
     * @param reqDTO 创建请求
     * @return 转账单编号
     */
    Long createTransfer(@Valid PayTransferCreateReqDTO reqDTO);

}
