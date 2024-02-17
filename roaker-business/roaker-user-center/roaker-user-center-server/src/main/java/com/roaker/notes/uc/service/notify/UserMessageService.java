package com.roaker.notes.uc.service.notify;


import com.roaker.notes.uc.vo.notify.MessageSendReq;
import com.roaker.notes.uc.vo.notify.MessageSendResp;
import com.roaker.notes.uc.vo.notify.NotifyResp;
import com.roaker.notes.uc.vo.notify.SmsSendReq;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface UserMessageService {
    NotifyResp sendSms(SmsSendReq smsSendReq);

    MessageSendResp sendMessage(MessageSendReq sendReq);
}
