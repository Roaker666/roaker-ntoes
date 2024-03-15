package com.roaker.notes.uc.vo.notify;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class SmsSendReq implements Serializable {
    @NotEmpty
    private String smsRecipient;
    @NotEmpty
    private String templateCode;
    private Map<String, Object> params;
}
