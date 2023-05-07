package com.roaker.notes.notify.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.roaker.notes.commons.excel.convert.DictConvert;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.NotifyChannelEnum;
import com.roaker.notes.enums.NotifyRecipientTypeEnum;
import com.roaker.notes.enums.SceneEnum;
import com.roaker.notes.notify.dal.dataobject.*;
import lombok.Data;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class NotifyTemplateDto {
    @ExcelProperty(value = "模板号码")
    private String templateCode;
    @ExcelProperty(value = "模板名称")
    private String templateName;
    @ExcelProperty(value = "场景用例", converter = DictConvert.class)
    private SceneEnum scene;
    @ExcelProperty(value = "PN")
    private NotifyPNDO notifyPn;
    @ExcelProperty(value = "AR")
    private NotifyARDO notifyAr;
    @ExcelProperty(value = "SMS")
    private NotifySmsDO notifySms;
    @ExcelProperty(value = "EMAIL")
    private NotifyEmailDO notifyEmail;
    @ExcelProperty(value = "收件人类型", converter = DictConvert.class)
    private NotifyRecipientTypeEnum recipientType;
    @ExcelProperty(value = "备注说明")
    private String remark;
    @ExcelProperty(value = "模板状态", converter = DictConvert.class)
    private CommonStatusEnum status;
    @ExcelProperty(value = "模板可发送渠道")
    private List<NotifyChannelEnum> enableChannelStatus;


    public static NotifyTemplateDto from(NotifyTemplateDO notifyTemplateDO, List<NotifySceneChannelDO> sceneChannelDO) {

    }
}
