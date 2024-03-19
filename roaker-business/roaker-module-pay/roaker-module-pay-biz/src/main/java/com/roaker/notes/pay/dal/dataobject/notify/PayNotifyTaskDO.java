package com.roaker.notes.pay.dal.dataobject.notify;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.pay.api.enums.notify.PayNotifyStatusEnum;
import com.roaker.notes.pay.api.enums.notify.PayNotifyTypeEnum;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.dal.dataobject.order.PayOrderDO;
import com.roaker.notes.pay.dal.dataobject.refund.PayRefundDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付通知
 * 在支付系统收到支付渠道的支付、退款的结果后，需要不断的通知到业务系统，直到成功。
 *
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_pay_notify_task")
@KeySequence("pay_notify_task_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PayNotifyTaskDO extends BaseDO {

    /**
     * 通知频率，单位为秒。
     * 算上首次的通知，实际是一共 1 + 8 = 9 次。
     */
    public static final Integer[] NOTIFY_FREQUENCY = new Integer[]{
            15, 15, 30, 180,
            1800, 1800, 1800, 3600
    };

    /**
     * 编号，自增
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
     * 通知类型
     * 外键 {@link PayNotifyTypeEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayNotifyTypeEnum payType;
    /**
     * 数据编号，根据不同 payType 进行关联：
     * 1. {@link PayNotifyTypeEnum#ORDER} 时，关联 {@link PayOrderDO#getId()}
     * 2. {@link PayNotifyTypeEnum#REFUND} 时，关联 {@link PayRefundDO#getId()}
     */
    @TableField
    private Long dataId;
    /**
     * 商户订单编号
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String merchantOrderId;
    /**
     * 商户转账单编号
     */
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 128)
    private String merchantTransferId;
    /**
     * 通知状态
     * 外键 {@link PayNotifyStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayNotifyStatusEnum payNotifyStatus;
    /**
     * 下一次通知时间
     */
    @TableField
    private LocalDateTime nextNotifyTime;
    /**
     * 最后一次执行时间
     */
    @TableField
    private LocalDateTime lastExecuteTime;
    /**
     * 当前通知次数
     */
    @TableField
    private Integer notifyTimes;
    /**
     * 最大可通知次数
     */
    @TableField
    private Integer maxNotifyTimes;
    /**
     * 通知地址
     */
    @TableField
    private String notifyUrl;
}
