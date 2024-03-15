package com.roaker.notes.uc.vo.notify;

import com.roaker.notes.enums.NotifyRecipientTypeEnum;
import com.roaker.notes.enums.SceneEnum;
import lombok.Data;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class ExportNotifyTemplateReqVO {
    private String templateCode;
    private String templateName;
    private SceneEnum scene;
    private NotifyRecipientTypeEnum recipientType;
}
