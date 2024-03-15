package com.roaker.notes.uc.dal.dataobject.notify;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.roaker.notes.commons.db.core.annotation.BizKey;
import com.roaker.notes.uc.service.notify.UserMessageReplaceable;
import com.roaker.notes.uc.dto.notify.EmailAttachmentFile;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_user_email_tab")
@KeySequence("sys_user_email_tab_seq")
@BizKey(bizName = "user_email", bizPrefix = "UE")
public class UserEmailMessageDO extends AbstractUserMessageDO implements UserMessageReplaceable, Serializable {

    @TableField
    private String fromAddress;
    @TableField
    private String emailRecipient;
    @TableField
    private String emailSubject;
    @TableField
    @ColumnType(value = MySqlTypeConstant.VARCHAR, length = 2048)
    private String emailContent;
    @TableField
    private Integer mailType;
    @TableField
    private String failMsg;
    @TableField
    private String filePathUrls;
    private List<EmailAttachmentFile> attachmentFileList;

    @Override
    public void replaceContent(Function<String, String> resolver) {
        setEmailSubject(resolver.apply(this.emailSubject));
        setEmailContent(resolver.apply(this.emailContent));
    }
}
