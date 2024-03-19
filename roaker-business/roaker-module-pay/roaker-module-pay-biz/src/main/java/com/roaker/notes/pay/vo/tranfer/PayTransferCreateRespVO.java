package com.roaker.notes.pay.vo.tranfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - 发起转账 Response VO")
@Data
@Accessors(chain = true)
public class PayTransferCreateRespVO {

    @Schema(description = "转账单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "转账状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1") // 参见 PayTransferStatusEnum 枚举
    private Integer status;

}
