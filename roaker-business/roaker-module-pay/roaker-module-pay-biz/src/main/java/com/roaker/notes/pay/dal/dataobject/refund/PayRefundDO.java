package com.roaker.notes.pay.dal.dataobject.refund;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.pay.api.enums.refund.PayRefundStatusEnum;
import com.roaker.notes.pay.core.client.dto.refund.PayRefundRespDTO;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.channel.PayChannelDO;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付退款单 DO
 * 一个支付订单，可以拥有多个支付退款单
 * 即 PayOrderDO : PayRefundDO = 1 : n
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_pay_refund")
@KeySequence("pay_refund_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayRefundDO extends BaseDO {

    /**
     * 退款单编号，数据库自增
     */
    @TableId
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 外部退款号，根据规则生成
     * 调用支付渠道时，使用该字段作为对接的退款号：
     * 1. 微信退款：对应 <a href="https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4">申请退款</a> 的 out_refund_no 字段
     * 2. 支付宝退款：对应 <a href="https://opendocs.alipay.com/open/02e7go"统一收单交易退款接口></a> 的 out_request_no 字段
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String no;

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
     * 商户编码
     * 枚举 {@link PayChannelEnum}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 32)
    private String channelCode;
    /**
     * 订单编号
     * 关联 {@link PayOrderDO#getId()}
     */
    @TableField
    private Long orderId;
    /**
     * 支付订单编号
     * 冗余 {@link PayOrderDO#getNo()}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String orderNo;

    // ========== 商户相关字段 ==========
    /**
     * 商户订单编号
     * 例如说，内部系统 A 的订单号，需要保证每个 PayAppDO 唯一
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String merchantOrderId;
    /**
     * 商户退款订单号
     * 例如说，内部系统 A 的订单号，需要保证每个 PayAppDO 唯一
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String merchantRefundId;
    /**
     * 异步通知地址
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String notifyUrl;

    // ========== 退款相关字段 ==========
    /**
     * 退款状态
     * 枚举 {@link PayRefundStatusEnum}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.INT)
    private PayRefundStatusEnum payRefundStatus;

    /**
     * 支付金额，单位：分
     */
    @TableField
    private Integer payPrice;
    /**
     * 退款金额，单位：分
     */
    @TableField
    private Integer refundPrice;

    /**
     * 退款原因
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String reason;

    /**
     * 用户 IP
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String userIp;

    // ========== 渠道相关字段 ==========
    /**
     * 渠道订单号
     * 冗余 {@link PayOrderDO#getChannelOrderNo()}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String channelOrderNo;
    /**
     * 渠道退款单号
     * 1. 微信退款：对应 <a href="https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4">申请退款</a> 的 refund_id 字段
     * 2. 支付宝退款：没有字段
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String channelRefundNo;
    /**
     * 退款成功时间
     */
    @TableField
    private LocalDateTime successTime;

    /**
     * 调用渠道的错误码
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 32)
    private String channelErrorCode;
    /**
     * 调用渠道的错误提示
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String channelErrorMsg;

    /**
     * 支付渠道的同步/异步通知的内容
     * 对应 {@link PayRefundRespDTO#getRawData()}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(value = MySqlTypeConstant.JSON)
    private String channelNotifyData;

}
