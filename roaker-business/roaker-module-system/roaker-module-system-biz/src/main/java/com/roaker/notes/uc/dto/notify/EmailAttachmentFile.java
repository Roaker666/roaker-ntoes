package com.roaker.notes.uc.dto.notify;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class EmailAttachmentFile implements Serializable {
    private String fileName;
    private String fileUrl;
    private Boolean encrypted;
}
