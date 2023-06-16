package com.roaker.notes.uc.dal.dataobject.notify;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.annotation.BizKey;
import com.roaker.notes.notify.common.UserMessageReplaceable;
import com.roaker.notes.notify.api.enums.MessageReadStatusEnum;
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
@TableName("user_ar_notice")
@KeySequence("user_ar_notice_seq")
@BizKey(bizName = "user-ar", bizPrefix = "UA")
@Builder
public class UserArNoticeDO extends AbstractUserMessageDO implements Serializable,UserMessageReplaceable {
    @TableField
    private String arTitle;
    @TableField
    private String arContent;
    @TableField
    private String arRedirect;
    @TableField
    private Integer arType;
    @TableField
    private String arBanner;
    @TableField
    private String arRedirectLabel;
    @TableField
    @ColumnType(MySqlTypeConstant.INT)
    private MessageReadStatusEnum readStatus;
    @Override
    public void replaceContent(Function<String, String> resolver) {
        setArTitle(resolver.apply(this.arTitle));
        setArContent(resolver.apply(this.arContent));
        setArRedirect(resolver.apply(this.arRedirect));
    }
}
