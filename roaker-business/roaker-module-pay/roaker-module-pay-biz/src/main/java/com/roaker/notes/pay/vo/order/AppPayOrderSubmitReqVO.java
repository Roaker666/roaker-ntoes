package com.roaker.notes.pay.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户 APP - 支付订单提交 Request VO")
@Data
public class AppPayOrderSubmitReqVO  extends PayOrderSubmitReqVO {
}
