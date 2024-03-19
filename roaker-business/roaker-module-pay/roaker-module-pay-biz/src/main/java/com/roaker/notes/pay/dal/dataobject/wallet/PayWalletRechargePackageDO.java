package com.roaker.notes.pay.dal.dataobject.wallet;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.*;

/**
 * 会员钱包充值套餐 DO
 * 通过充值套餐时，可以赠送一定金额；
 * @author lei.rao
 * @since 1.0
 */
@TableName(value ="sys_pay_wallet_recharge_package")
@KeySequence("pay_wallet_recharge_package_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayWalletRechargePackageDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 套餐名
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String name;

    /**
     * 支付金额
     */
    @TableField
    private Integer payPrice;
    /**
     * 赠送金额
     */
    @TableField
    private Integer bonusPrice;

    /**
     * 状态
     * 枚举 {@link CommonStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
}
