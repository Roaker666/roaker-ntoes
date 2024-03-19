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
import com.roaker.notes.pay.api.enums.wallet.PayWalletBizTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 会员钱包流水 DO
 *
 * @author lei.rao
 * @since 1.0
 */
@TableName(value = "pay_wallet_transaction")
@KeySequence("pay_wallet_transaction_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayWalletTransactionDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    @IsAutoIncrement
    @IsNotNull
    private Long id;

    /**
     * 流水号
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String no;

    /**
     * 钱包编号
     * 关联 {@link PayWalletDO#getId()}
     */
    @TableField
    private Long walletId;

    /**
     * 关联业务分类
     * 枚举 {@link PayWalletBizTypeEnum#getCode()}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayWalletBizTypeEnum bizType;

    /**
     * 关联业务编号
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String bizId;

    /**
     * 流水说明
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String title;

    /**
     * 交易金额，单位分
     * 正值表示余额增加，负值表示余额减少
     */
    @TableField
    private Integer price;

    /**
     * 交易后余额，单位分
     */
    @TableField
    private Integer balance;
}
