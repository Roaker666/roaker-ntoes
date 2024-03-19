package com.roaker.notes.pay.api.core.notify.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 转账单的通知 Request DTO
 * @author lei.rao
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class PayTransferNotifyReqDTO {

    /**
     * 商户转账单号
     */
    @NotEmpty(message = "商户转账单号不能为空")
    private String merchantTransferId;

    /**
     * 转账订单编号
     */
    @NotNull(message = "转账订单编号不能为空")
    private Long payTransferId;
}
