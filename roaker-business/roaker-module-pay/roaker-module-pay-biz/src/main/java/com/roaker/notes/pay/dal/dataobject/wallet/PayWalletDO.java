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
import com.roaker.notes.enums.UserTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 会员钱包 DO
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_pay_wallet")
@KeySequence("pay_wallet") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayWalletDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    @IsAutoIncrement
    @IsNotNull
    private Long id;

    /**
     * 用户 id
     * 关联 MemberUserDO 的 id 编号
     * 关联 AdminUserDO 的 id 编号
     */
    @TableField
    private String userId;
    /**
     * 用户类型, 预留 多商户转帐可能需要用到
     * 关联 {@link UserTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private UserTypeEnum userType;

    /**
     * 余额，单位分
     */
    @TableField
    private Integer balance;

    /**
     * 冻结金额，单位分
     */
    @TableField
    private Integer freezePrice;

    /**
     * 累计支出，单位分
     */
    @TableField
    private Integer totalExpense;
    /**
     * 累计充值，单位分
     */
    @TableField
    private Integer totalRecharge;
}
