package com.roaker.notes.uc.dal.dataobject.notify;


import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsNotNull;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.NotifyChannelEnum;
import com.roaker.notes.enums.SceneEnum;
import lombok.*;

@Data
@TableName("notify_scene_channel")
@KeySequence("notify_scene_channel_seq") //用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifySceneChannelDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    @IsAutoIncrement
    @IsNotNull
    private Long id;
    @TableField
    @ColumnType(MySqlTypeConstant.VARCHAR)
    private String templateCode;

    /**
     * 消息配置场景
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private SceneEnum scene;
    /**
     * 消息特定场景,表示该场景发送的渠道(SMS...)
     */
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private NotifyChannelEnum notifyChannel;

    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private CommonStatusEnum status;
}
