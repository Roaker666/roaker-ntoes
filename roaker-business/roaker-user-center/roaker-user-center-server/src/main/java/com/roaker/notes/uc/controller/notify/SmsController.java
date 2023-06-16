package com.roaker.notes.uc.controller.notify;

import com.roaker.notes.commons.idempotent.annotation.Idempotent;
import com.roaker.notes.commons.idempotent.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.roaker.notes.notify.api.vo.NotifyResp;
import com.roaker.notes.notify.api.vo.SmsSendReq;
import com.roaker.notes.notify.service.UserMessageService;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lei.rao
 * @since 1.0
 */
@Tag(name = "消息中心 - SMS发送")
@RestController
@RequestMapping("/notify/sms")
@Validated
@Slf4j
@RequiredArgsConstructor
public class SmsController {
    private final UserMessageService userMessageService;

    @PostMapping("/send")
    @Idempotent(timeout = 3, keyResolver = ExpressionIdempotentKeyResolver.class, keyArg = ":otp:#smsSendReq.smsRecipient")
    public CommonResult<NotifyResp> sendSms(@RequestBody SmsSendReq smsSendReq) {
        return CommonResult.success(userMessageService.sendSms(smsSendReq));
    }
}
