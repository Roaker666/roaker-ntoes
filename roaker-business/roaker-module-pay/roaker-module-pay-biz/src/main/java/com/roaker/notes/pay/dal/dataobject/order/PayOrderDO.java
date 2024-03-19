package com.roaker.notes.pay.dal.dataobject.order;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.pay.api.enums.order.PayOrderStatusEnum;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.channel.PayChannelDO;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付订单 DO
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_pay_order")
@KeySequence("pay_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayOrderDO extends BaseDO {

    /**
     * 订单编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 应用编号
     * 关联 {@link PayAppDO#getId()}
     */
    @TableField
    private Long appId;
    /**
     * 渠道编号
     * 关联 {@link PayChannelDO#getId()}
     */
    @TableField
    private Long channelId;
    /**
     * 渠道编码
     * 枚举 {@link PayChannelEnum}
     */
    @TableField
    private String channelCode;

    // ========== 商户相关字段 ==========

    /**
     * 商户订单编号
     * 例如说，内部系统 A 的订单号，需要保证每个 PayAppDO 唯一
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String merchantOrderId;
    /**
     * 商品标题
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String subject;
    /**
     * 商品描述信息
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR)
    private String body;
    /**
     * 异步通知地址
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String notifyUrl;

    // ========== 订单相关字段 ==========

    /**
     * 支付金额，单位：分
     */
    @TableField
    private Integer price;
    /**
     * 渠道手续费，单位：百分比
     * 冗余 {@link PayChannelDO#getFeeRate()}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.DOUBLE)
    private Double channelFeeRate;
    /**
     * 渠道手续金额，单位：分
     */
    @TableField
    private Integer channelFeePrice;
    /**
     * 支付状态
     * 枚举 {@link PayOrderStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayOrderStatusEnum payOrderStatus;
    /**
     * 用户 IP
     */
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    @TableField
    private String userIp;
    /**
     * 订单失效时间
     */
    @TableField
    private LocalDateTime expireTime;
    /**
     * 订单支付成功时间
     */
    @TableField
    private LocalDateTime successTime;
    /**
     * 支付成功的订单拓展单编号
     * 关联 {@link PayOrderExtensionDO#getId()}
     */
    @TableField
    private Long extensionId;
    /**
     * 支付成功的外部订单号
     * 关联 {@link PayOrderExtensionDO#getNo()}
     */
    @TableField
    private String no;

    // ========== 退款相关字段 ==========
    /**
     * 退款总金额，单位：分
     */
    @TableField
    private Integer refundPrice;

    // ========== 渠道相关字段 ==========
    /**
     * 渠道用户编号
     *
     * 例如说，微信 openid、支付宝账号
     */
    @TableField
    private String channelUserId;
    /**
     * 渠道订单号
     */
    @TableField
    private String channelOrderNo;

}
