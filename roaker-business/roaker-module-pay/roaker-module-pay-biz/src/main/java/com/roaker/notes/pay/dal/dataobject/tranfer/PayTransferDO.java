package com.roaker.notes.pay.dal.dataobject.tranfer;

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
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.core.enums.transfer.PayTransferStatusRespEnum;
import com.roaker.notes.pay.core.enums.transfer.PayTransferTypeEnum;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.channel.PayChannelDO;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_pay_transfer")
@KeySequence("pay_transfer_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayTransferDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    @IsAutoIncrement
    @IsNotNull
    private Long id;

    /**
     * 转账单号
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
     * 转账渠道编号
     * 关联 {@link PayChannelDO#getId()}
     */
    @TableField
    private Long channelId;

    /**
     * 转账渠道编码
     * 枚举 {@link PayChannelEnum}
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 32)
    private String channelCode;

    // ========== 商户相关字段 ==========
    /**
     * 商户转账单编号
     * 例如说，内部系统 A 的订单号，需要保证每个 PayAppDO 唯一
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String merchantTransferId;

    // ========== 转账相关字段 ==========

    /**
     * 类型
     * 枚举 {@link PayTransferTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayTransferTypeEnum payType;

    /**
     * 转账标题
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String subject;

    /**
     * 转账金额，单位：分
     */
    @TableField
    private Integer price;

    /**
     * 收款人姓名
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String userName;

    /**
     * 转账状态
     * 枚举 {@link PayTransferStatusRespEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayTransferStatusRespEnum payTransferStatus;

    /**
     * 订单转账成功时间
     */
    @TableField
    private LocalDateTime successTime;

    // ========== 支付宝转账相关字段 ==========
    /**
     * 支付宝登录号
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String alipayLogonId;


    // ========== 微信转账相关字段 ==========
    /**
     * 微信 openId
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String openid;

    // ========== 其它字段 ==========

    /**
     * 异步通知地址
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 255)
    private String notifyUrl;

    /**
     * 用户 IP
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String userIp;

    /**
     * 渠道的额外参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> channelExtras;

    /**
     * 渠道转账单号
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String channelTransferNo;

    /**
     * 调用渠道的错误码
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 64)
    private String channelErrorCode;
    /**
     * 调用渠道的错误提示
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String channelErrorMsg;

    /**
     * 渠道的同步/异步通知的内容
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(value = MySqlTypeConstant.JSON)
    private String channelNotifyData;

}
