package com.roaker.notes.uc.service.notify;

import com.roaker.notes.notify.api.vo.MessageSendReq;
import com.roaker.notes.notify.api.vo.MessageSendResp;
import com.roaker.notes.notify.api.vo.NotifyResp;
import com.roaker.notes.notify.api.vo.SmsSendReq;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface UserMessageService {
    NotifyResp sendSms(SmsSendReq smsSendReq);

    MessageSendResp sendMessage(MessageSendReq sendReq);
}
