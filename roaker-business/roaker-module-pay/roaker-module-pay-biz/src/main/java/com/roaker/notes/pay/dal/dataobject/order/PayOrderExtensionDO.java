package com.roaker.notes.pay.dal.dataobject.order;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.pay.api.enums.order.PayOrderStatusEnum;
import com.roaker.notes.pay.core.client.dto.order.PayOrderRespDTO;
import com.roaker.notes.pay.dal.dataobject.channel.PayChannelDO;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 支付订单拓展 DO
 * 每次调用支付渠道，都会生成一条对应记录
 *
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "sys_pay_order_extension", autoResultMap = true)
@KeySequence("pay_order_extension_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayOrderExtensionDO extends BaseDO {
    /**
     * 订单拓展编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 外部订单号，根据规则生成
     * 调用支付渠道时，使用该字段作为对接的订单号：
     * 1. 微信支付：对应 <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml">JSAPI 支付</a> 的 out_trade_no 字段
     * 2. 支付宝支付：对应 <a href="https://opendocs.alipay.com/open/270/105898">电脑网站支付</a> 的 out_trade_no 字段
     * 例如说，P202110132239124200055
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String no;
    /**
     * 订单号
     * <p>
     * 关联 {@link PayOrderDO#getId()}
     */
    @TableField
    private Long orderId;
    /**
     * 渠道编号
     * <p>
     * 关联 {@link PayChannelDO#getId()}
     */
    @TableField
    private Long channelId;
    /**
     * 渠道编码
     */
    @TableField
    private String channelCode;
    /**
     * 用户 IP
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String userIp;
    /**
     * 支付状态
     * <p>
     * 枚举 {@link PayOrderStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayOrderStatusEnum payStatus;
    /**
     * 支付渠道的额外参数
     * <p>
     * 参见 <a href="https://www.pingxx.com/api/支付渠道%20extra%20参数说明.html">参数说明</>
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> channelExtras;

    /**
     * 调用渠道的错误码
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 32)
    private String channelErrorCode;
    /**
     * 调用渠道报错时，错误信息
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String channelErrorMsg;

    /**
     * 支付渠道的同步/异步通知的内容
     * <p>
     * 对应 {@link PayOrderRespDTO#getRawData()}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.JSON)
    private String channelNotifyData;
}
