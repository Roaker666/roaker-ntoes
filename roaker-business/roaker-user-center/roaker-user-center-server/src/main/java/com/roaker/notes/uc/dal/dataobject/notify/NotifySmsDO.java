package com.roaker.notes.uc.dal.dataobject.notify;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NotifySmsDO implements Serializable {
    @ExcelProperty({"SMS","短信内容模板"})
    private String smsContent;
    @ExcelProperty({"SMS","短信固定收件人"})
    private String smsRecipient;
}
