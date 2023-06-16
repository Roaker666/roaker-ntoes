package com.roaker.notes.uc.vo.notify;

import com.roaker.notes.commons.db.core.dataobject.PageParam;
import com.roaker.notes.enums.NotifyRecipientTypeEnum;
import com.roaker.notes.enums.SceneEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lei.rao
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryNotifyTemplatePageReqVO extends PageParam {
    private String templateCode;
    private String templateName;
    private SceneEnum scene;
    private NotifyRecipientTypeEnum recipientType;
}
