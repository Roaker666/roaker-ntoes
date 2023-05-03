package com.roaker.notes.notify.dal.dataobject;

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
    private String emailSubject;
    private String emailContent;
    private String emailAttachment;
    private Integer emailAttachmentNum;
    private String emailRecipient;
}
