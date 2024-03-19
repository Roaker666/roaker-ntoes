package com.roaker.notes.pay.dal.dataobject.wallet;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.pay.api.enums.refund.PayRefundStatusEnum;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.dal.dataobject.refund.PayRefundDO;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 会员钱包充值
 * @author lei.rao
 * @since 1.0
 */
@TableName(value ="sys_pay_wallet_recharge")
@KeySequence("pay_wallet_recharge_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayWalletRechargeDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    @IsAutoIncrement
    @IsNotNull
    private Long id;

    /**
     * 钱包编号
     * 关联 {@link PayWalletDO#getId()}
     */
    @TableField
    private Long walletId;

    /**
     * 用户实际到账余额
     * 例如充 100 送 20，则该值是 120
     */
    @TableField
    private Integer totalPrice;
    /**
     * 实际支付金额
     */
    @TableField
    private Integer payPrice;
    /**
     * 钱包赠送金额
     */
    @TableField
    private Integer bonusPrice;

    /**
     * 充值套餐编号
     * 关联 {@link PayWalletRechargeDO#getPackageId()} 字段
     */
    @TableField
    private Long packageId;

    /**
     * 是否已支付
     * true - 已支付
     * false - 未支付
     */
    @TableField
    private Boolean payStatus;

    /**
     * 支付订单编号
     * 关联 {@link PayOrderDO#getId()}
     */
    @TableField
    private Long payOrderId;

    /**
     * 支付成功的支付渠道
     * 冗余 {@link PayOrderDO#getChannelCode()}
     */
    @TableField
    private String payChannelCode;
    /**
     * 订单支付时间
     */
    @TableField
    private LocalDateTime payTime;

    /**
     * 支付退款单编号
     * 关联 {@link PayRefundDO#getId()}
     */
    @TableField
    private Long payRefundId;

    /**
     * 退款金额，包含赠送金额
     */
    @TableField
    private Integer refundTotalPrice;
    /**
     * 退款支付金额
     */
    @TableField
    private Integer refundPayPrice;

    /**
     * 退款钱包赠送金额
     */
    @TableField
    private Integer refundBonusPrice;

    /**
     * 退款时间
     */
    @TableField
    private LocalDateTime refundTime;

    /**
     * 退款状态
     * 枚举 {@link PayRefundStatusEnum}
     */
    @TableField
    private PayRefundStatusEnum refundStatus;
}
