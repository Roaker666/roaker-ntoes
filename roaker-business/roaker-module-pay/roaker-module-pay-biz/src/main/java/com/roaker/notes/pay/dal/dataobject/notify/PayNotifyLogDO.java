package com.roaker.notes.pay.dal.dataobject.notify;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.pay.api.enums.notify.PayNotifyStatusEnum;
import lombok.*;

/**
 * 商户支付、退款等的通知 Log
 * 每次通知时，都会在该表中，记录一次 Log，方便排查问题
 * @author lei.rao
 * @since 1.0
 */
@TableName("sys_pay_notify_log")
@KeySequence("pay_notify_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayNotifyLogDO extends BaseDO {
    /**
     * 日志编号，自增
     */
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    /**
     * 通知任务编号
     * 关联 {@link PayNotifyTaskDO#getId()}
     */
    @TableField
    private Long taskId;
    /**
     * 第几次被通知
     * 对应到 {@link PayNotifyTaskDO#getNotifyTimes()}
     */
    @TableField
    private Integer notifyTimes;
    /**
     * HTTP 响应结果
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private String response;
    /**
     * 支付通知状态
     * 外键 {@link PayNotifyStatusEnum}
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private PayNotifyStatusEnum payStatus;
}
