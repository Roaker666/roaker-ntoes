package com.roaker.notes.uc.api.notify;

import com.roaker.notes.uc.vo.notify.MessageSendReq;
import com.roaker.notes.uc.vo.notify.MessageSendResp;
import com.roaker.notes.uc.vo.notify.NotifyResp;
import com.roaker.notes.uc.vo.notify.SmsSendReq;
import com.roaker.notes.vo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.roaker.notes.commons.constants.ApplicationNameConstants.MSG_NAME;

/**
 * @author lei.rao
 * @since 1.0
 */
@FeignClient(name = MSG_NAME, fallbackFactory = MessageApiFallback.class, dismiss404 = true)
public interface MessageApi {

    @PostMapping("/notify/sms/send")
    CommonResult<NotifyResp> sendSms(@RequestBody SmsSendReq smsSendReq);

    @PostMapping("/notify/msg/send")
    CommonResult<MessageSendResp> sendSms(@RequestBody MessageSendReq messageSendReq);
}
