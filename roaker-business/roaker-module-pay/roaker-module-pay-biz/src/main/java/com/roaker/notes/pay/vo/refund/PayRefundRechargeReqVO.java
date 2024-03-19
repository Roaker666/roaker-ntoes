package com.roaker.notes.pay.vo.refund;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class PayRefundRechargeReqVO implements Serializable {
    private Long id;
    private Long channelId;
    private String reason;
}
