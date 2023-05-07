package com.roaker.notes.notify.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@Schema(description = "消息发送请求")
public class MessageSendReq {
    @Schema(description = "请求唯一ID,用于幂等检查")
    private String requestId;

    private String smsRecipient;

    private String emailRecipient;

    private String templateCode;

    private Long uid;

    private Map<String, Object> params;
}
