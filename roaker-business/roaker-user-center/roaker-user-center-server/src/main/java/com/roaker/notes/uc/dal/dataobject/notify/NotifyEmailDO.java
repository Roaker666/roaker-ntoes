package com.roaker.notes.uc.dal.dataobject.notify;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NotifyEmailDO implements Serializable {
    @ExcelProperty({"EMAIL","邮件主题"})
    private String emailSubject;
    @ExcelProperty({"EMAIL","邮件内容"})
    private String emailContent;
    @ExcelProperty({"EMAIL","邮件附件数量"})
    private Integer emailAttachmentNum;
    @ExcelProperty({"EMAIL","邮件固定收件人"})
    private String emailRecipient;
    @ExcelProperty({"EMAIL","发件人固定地址"})
    private String fromAddress;
}
