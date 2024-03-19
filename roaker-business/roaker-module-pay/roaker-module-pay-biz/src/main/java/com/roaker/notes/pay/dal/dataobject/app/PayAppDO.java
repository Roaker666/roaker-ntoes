package com.roaker.notes.pay.dal.dataobject.app;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 支付应用 DO
 * 一个商户下，可能会有多个支付应用。例如说，京东有京东商城、京东到家等等
 * 不过一般来说，一个商户，只有一个应用哈~
 * 即 PayMerchantDO : PayAppDO = 1 : n
 *
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_pay_app")
@KeySequence("pay_app_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayAppDO extends BaseDO {
    /**
     * 应用编号，数据库自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 应用名
     */
    @TableField
    private String name;
    /**
     * 状态
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
    /**
     * 备注
     */
    @TableField
    private String remark;
    /**
     * 支付结果的回调地址
     */
    @TableField
    private String orderNotifyUrl;
    /**
     * 退款结果的回调地址
     */
    @TableField
    private String refundNotifyUrl;

    /**
     * 转账结果的回调地址
     */
    @TableField
    private String transferNotifyUrl;
}
