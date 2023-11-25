package com.roaker.notes.uc.api.notify;

import com.roaker.notes.notify.api.vo.MessageSendReq;
import com.roaker.notes.notify.api.vo.MessageSendResp;
import com.roaker.notes.notify.api.vo.NotifyResp;
import com.roaker.notes.notify.api.vo.SmsSendReq;
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
