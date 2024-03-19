package com.roaker.notes.pay.vo.wallet.transaction;

import com.roaker.notes.commons.db.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理后台 - 钱包流水分页 Request VO")
@Data
public class PayWalletTransactionPageReqVO extends PageParam {

    @Schema(description = "钱包编号",  example = "1")
    private Long walletId;

}
