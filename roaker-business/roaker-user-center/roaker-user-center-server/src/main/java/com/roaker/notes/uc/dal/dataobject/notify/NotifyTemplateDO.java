package com.roaker.notes.uc.dal.dataobject.notify;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.annotation.Unique;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.NotifyRecipientTypeEnum;
import com.roaker.notes.enums.SceneEnum;
import lombok.*;

@Data
@TableName("sys_notify_template")
@KeySequence("sys_notify_template_seq") //用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyTemplateDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;

    /**
     * 模板Code
     */
    @TableField
    @Unique
    private String templateCode;
    /**
     * 模板名称
     */
    @TableField
    private String templateName;
    /**
     * 针对场景
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private SceneEnum scene;
    /**
     * PN推送 模板配置
     */
    @ColumnType(MySqlTypeConstant.JSON)
    @TableField(typeHandler = JacksonTypeHandler.class)
    private NotifyPNDO notifyPn;

    /**
     * AR站内信 模板配置
     */
    @ColumnType(MySqlTypeConstant.JSON)
    @TableField(typeHandler = JacksonTypeHandler.class)
    private NotifyARDO notifyAr;
    /**
     * sms 配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private NotifySmsDO notifySms;
    /**
     * email 配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private NotifyEmailDO notifyEmail;
    /**
     * 收件人类型
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private NotifyRecipientTypeEnum recipientType;

    @TableField
    private String remark;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
}
