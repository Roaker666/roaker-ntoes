package com.roaker.notes.notify.dal.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.annotation.BizKey;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.notify.api.enums.MessageSendStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public abstract class AbstractUserMessageDO extends BaseDO implements Serializable {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    protected Long id;
    @TableId(type = IdType.ASSIGN_UUID)
    protected String msgId;
    @TableField
    protected Long uid;
    @TableField
    protected String templateCode;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    protected MessageSendStatusEnum sendStatus;
    @TableField
    protected Integer retrySendTimes;
    @TableField
    protected LocalDateTime sendTime;
}
