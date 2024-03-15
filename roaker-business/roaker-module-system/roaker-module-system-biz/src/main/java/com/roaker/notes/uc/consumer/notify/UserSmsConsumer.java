package com.roaker.notes.uc.consumer.notify;

import com.roaker.notes.commons.core.KeyValue;
import com.roaker.notes.starters.sms.core.client.SmsClient;
import com.roaker.notes.starters.sms.core.client.SmsClientFactory;
import com.roaker.notes.starters.sms.core.client.SmsCommonResult;
import com.roaker.notes.starters.sms.core.client.dto.SmsSendRespDTO;
import com.roaker.notes.starters.sms.core.property.SmsChannelProperties;
import com.roaker.notes.uc.dal.dataobject.notify.UserSmsMessageDO;
import com.roaker.notes.uc.dal.mapper.notify.UserSmsMessageMapper;
import com.roaker.notes.uc.enums.notify.MessageSendStatusEnum;
import com.roaker.notes.uc.enums.notify.NotifyCommonConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.common.schema.SchemaType;
import org.slf4j.Logger;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

import java.util.Collections;


/**
 * @author lei.rao
 * @since 1.0
 */
@Component
@Slf4j
@PulsarListener(topics = "${" + NotifyCommonConstants.SMS_TOPIC + "}", schemaType = SchemaType.JSON, subscriptionName = "${" + NotifyCommonConstants.KEY_CONSUMER_GROUP + "}")
@RequiredArgsConstructor
public class UserSmsConsumer extends AbstractMessageMQListener<UserSmsMessageDO> {
    private final SmsChannelProperties smsChannelProperties;
    private final UserSmsMessageMapper userSmsMessageMapper;
    private final SmsClientFactory smsClientFactory;

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    protected String logPrefix() {
        return "【SMS Message Consumer】";
    }

    @Override
    protected void doHandleMessage(UserSmsMessageDO msg) {

        SmsClient smsClient = smsClientFactory.getSmsClient(smsChannelProperties.getId());

        SmsCommonResult<SmsSendRespDTO> commonResult =
                smsClient.sendSms(msg.getMsgId(), msg.getSmsRecipient(), msg.getTemplateCode(), Collections.singletonList(new KeyValue<>("content", msg.getSmsContent())));
        msg.setSendStatus(MessageSendStatusEnum.SEND_FAILURE);
        if (commonResult != null &&
                commonResult.isSuccess()) {
            msg.setSendStatus(MessageSendStatusEnum.SEND_SUCCESS);
        }
        userSmsMessageMapper.insert(msg);
    }
}
