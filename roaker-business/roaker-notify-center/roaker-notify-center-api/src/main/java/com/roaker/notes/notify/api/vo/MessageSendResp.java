package com.roaker.notes.notify.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class MessageSendResp implements Serializable {
    private String smsMsgId;
    private String arMsgId;
    private String pnMsgId;
    private String emailMsgId;
}
