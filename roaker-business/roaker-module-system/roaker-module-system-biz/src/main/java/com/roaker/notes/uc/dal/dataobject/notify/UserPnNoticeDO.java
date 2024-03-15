package com.roaker.notes.uc.dal.dataobject.notify;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.annotation.BizKey;
import com.roaker.notes.uc.service.notify.UserMessageReplaceable;
import lombok.*;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_pn_notice")
@KeySequence("sys_user_pn_notice_seq")
@BizKey(bizName = "user-pn", bizPrefix = "UP")
@Builder
public class UserPnNoticeDO extends AbstractUserMessageDO implements Serializable, UserMessageReplaceable {
    @TableField
    private String pnTitle;
    @TableField
    private String pnContent;
    @TableField
    private String pnRedirect;
    @TableField
    private String pnIcon;
    @TableField
    private String pnBanner;
    @TableField
    private String failMsg;
    /**
     * 推送组建的额外信息
     * 如设备ID/系统/参数等
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(MySqlTypeConstant.JSON)
    private String extMsg;

    @Override
    public void replaceContent(Function<String, String> resolver) {
        setPnTitle(resolver.apply(this.pnTitle));
        setPnContent(resolver.apply(this.pnContent));
    }
}
