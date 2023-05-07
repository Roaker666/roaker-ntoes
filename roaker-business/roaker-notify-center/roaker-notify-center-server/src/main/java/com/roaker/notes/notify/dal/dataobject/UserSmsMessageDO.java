package com.roaker.notes.notify.dal.dataobject;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.roaker.notes.commons.db.core.annotation.BizKey;
import com.roaker.notes.notify.common.UserMessageReplaceable;
import lombok.*;
import lombok.experimental.Accessors;

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
@TableName("user_sms_tab")
@KeySequence("user_sms_tab")
@BizKey(bizName = "user-sms", bizPrefix = "US")
@Builder
@Accessors(chain = true)
public class UserSmsMessageDO extends AbstractUserMessageDO implements UserMessageReplaceable,Serializable {
    @TableField
    private String smsContent;
    @TableField
    private String smsRecipient;
    @TableField
    private String failMsg;
    @Override
    public void replaceContent(Function<String, String> resolver) {
        setSmsContent(resolver.apply(this.smsContent));
    }
}
