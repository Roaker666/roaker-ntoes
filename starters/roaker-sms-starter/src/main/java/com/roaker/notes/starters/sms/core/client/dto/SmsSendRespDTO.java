package com.roaker.notes.starters.sms.core.client.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class SmsSendRespDTO {

    /**
     * 短信 API 发送返回的序号
     */
    private String serialNo;

}