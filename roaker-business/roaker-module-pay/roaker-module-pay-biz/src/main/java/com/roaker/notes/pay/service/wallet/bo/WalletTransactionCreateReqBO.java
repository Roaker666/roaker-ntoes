package com.roaker.notes.pay.service.wallet.bo;

import com.roaker.notes.commons.validation.InEnum;
import com.roaker.notes.pay.api.enums.wallet.PayWalletBizTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 创建钱包流水 BO
 *
 */
@Data
@Accessors(chain = true)
public class WalletTransactionCreateReqBO {

    /**
     * 钱包编号
     *
     */
    @NotNull(message = "钱包编号不能为空")
    private Long walletId;

    /**
     * 交易金额，单位分
     * 正值表示余额增加，负值表示余额减少
     */
    @NotNull(message = "交易金额不能为空")
    private Integer price;

    /**
     * 交易后余额，单位分
     */
    @NotNull(message = "交易后余额不能为空")
    private Integer balance;

    /**
     * 关联业务分类
     * 枚举 {@link PayWalletBizTypeEnum#getCode()} ()}
     */
    @NotNull(message = "关联业务分类不能为空")
    @InEnum(PayWalletBizTypeEnum.class)
    private Integer bizType;

    /**
     * 关联业务编号
     */
    @NotEmpty(message = "关联业务编号不能为空")
    private String bizId;

    /**
     * 流水说明
     */
    @NotEmpty(message = "流水说明不能为空")
    private String title;
}
