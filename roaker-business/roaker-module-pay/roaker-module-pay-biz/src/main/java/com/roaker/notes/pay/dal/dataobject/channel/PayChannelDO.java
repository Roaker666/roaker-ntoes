package com.roaker.notes.pay.dal.dataobject.channel;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.pay.core.client.PayClientConfig;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 支付渠道 DO
 * 一个应用下，会有多种支付渠道，例如说微信支付、支付宝支付等等
 * 即 PayAppDO : PayChannelDO = 1 : n
 *
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "sys_pay_channel", autoResultMap = true)
@KeySequence("pay_channel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayChannelDO extends BaseDO {
    /**
     * 渠道编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 渠道编码
     * 枚举 {@link PayChannelEnum}
     */
    @TableField
    private String code;
    /**
     * 状态
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 渠道费率，单位：百分比
     */
    @TableField
    @ColumnType(MySqlTypeConstant.DOUBLE)
    private Double feeRate;
    /**
     * 备注
     */
    @TableField
    private String remark;

    /**
     * 应用编号
     * 关联 {@link PayAppDO#getId()}
     */
    @TableField
    private Long appId;
    /**
     * 支付渠道配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private PayClientConfig config;
}
