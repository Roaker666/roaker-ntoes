package com.roaker.notes.uc.api.notify;

import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.notify.api.vo.MessageSendReq;
import com.roaker.notes.notify.api.vo.MessageSendResp;
import com.roaker.notes.notify.api.vo.NotifyResp;
import com.roaker.notes.notify.api.vo.SmsSendReq;
import com.roaker.notes.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lei.rao
 * @since 1.0
 */
@Component
@Slf4j
public class MessageApiFallback implements FallbackFactory<MessageApi> {
    @Override
    public MessageApi create(Throwable cause) {
        return new MessageApi() {

            @Override
            public CommonResult<NotifyResp> sendSms(SmsSendReq smsSendReq) {
                log.info("MSG Server client initiate fallback caused by :{}", cause.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.MSG_SERVER_ERROR);
            }

            @Override
            public CommonResult<MessageSendResp> sendSms(MessageSendReq messageSendReq) {
                log.info("MSG Server client initiate fallback caused by :{}", cause.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.MSG_SERVER_ERROR);
            }
        };
    }
}
