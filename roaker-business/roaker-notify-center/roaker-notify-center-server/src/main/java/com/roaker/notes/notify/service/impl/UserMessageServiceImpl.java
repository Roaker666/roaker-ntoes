package com.roaker.notes.notify.service.impl;

import com.roaker.notes.commons.constants.ErrorCodeConstants;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.commons.utils.StrUtils;
import com.roaker.notes.enums.NotifyChannelEnum;
import com.roaker.notes.enums.NotifyRecipientTypeEnum;
import com.roaker.notes.notify.api.enums.MessageSendStatusEnum;
import com.roaker.notes.notify.api.enums.NotifyCommonConstants;
import com.roaker.notes.notify.api.vo.MessageSendReq;
import com.roaker.notes.notify.api.vo.MessageSendResp;
import com.roaker.notes.notify.api.vo.NotifyResp;
import com.roaker.notes.notify.api.vo.SmsSendReq;
import com.roaker.notes.notify.common.FreemarkerReplaceUtils;
import com.roaker.notes.notify.dal.dataobject.UserArNoticeDO;
import com.roaker.notes.notify.dal.dataobject.UserEmailMessageDO;
import com.roaker.notes.notify.dal.dataobject.UserPnNoticeDO;
import com.roaker.notes.notify.dal.dataobject.UserSmsMessageDO;
import com.roaker.notes.notify.dto.NotifyTemplateDto;
import com.roaker.notes.notify.service.NotifyTemplateService;
import com.roaker.notes.notify.service.UserMessageService;
import com.roaker.notes.seq.SeqClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserMessageServiceImpl implements UserMessageService {
    private final NotifyTemplateService notifyTemplateService;
    private final RocketMQTemplate rocketMQTemplate;
    @Value("${" + NotifyCommonConstants.SMS_TOPIC + "}")
    private String smsTopic;
    @Value("${" + NotifyCommonConstants.MAIL_TOPIC + "}")
    private String emailTopic;
    @Value("${" + NotifyCommonConstants.AR_TOPIC + "}")
    private String arTopic;
    @Value("${" + NotifyCommonConstants.PN_TOPIC + "}")
    private String pnTopic;
    private final SeqClient seqClient;


    @Override
    public NotifyResp sendSms(SmsSendReq smsSendReq) {
        NotifyTemplateDto notifyTemplateDto = notifyTemplateService.getNotifyTemplate(smsSendReq.getTemplateCode());
        if (notifyTemplateDto == null) {
            throw exception(ErrorCodeConstants.MSG_TEMPLATE_NOT_EXISTS);
        }
        return new NotifyResp(sendSms(notifyTemplateDto, smsSendReq.getSmsRecipient(), smsSendReq.getTemplateCode(), smsSendReq.getParams()));
    }


    @Override
    public MessageSendResp sendMessage(MessageSendReq sendReq) {
        NotifyTemplateDto notifyTemplateDto = notifyTemplateService.getNotifyTemplate(sendReq.getTemplateCode());
        if (notifyTemplateDto == null) {
            throw exception(ErrorCodeConstants.MSG_TEMPLATE_NOT_EXISTS);
        }
        MessageSendResp resp = new MessageSendResp();

        if (CollectionUtils.isNotEmpty(notifyTemplateDto.getEnableChannelStatus())) {
            for (NotifyChannelEnum notifyChannel : notifyTemplateDto.getEnableChannelStatus()) {
                switch (notifyChannel) {
                    case SMS ->
                            resp.setSmsMsgId(sendSms(notifyTemplateDto, sendReq.getSmsRecipient(), sendReq.getTemplateCode(), sendReq.getParams()));
                    case MAIL ->
                            resp.setEmailMsgId(sendMail(notifyTemplateDto, sendReq.getUid(), sendReq.getEmailRecipient(), sendReq.getTemplateCode(), sendReq.getParams()));
                    case AR ->
                            resp.setArMsgId(sendPn(notifyTemplateDto, sendReq.getUid(), sendReq.getTemplateCode(), sendReq.getParams()));
                    case PN ->
                            resp.setPnMsgId(sendPn(notifyTemplateDto, sendReq.getUid(), sendReq.getTemplateCode(), sendReq.getParams()));
                    default -> throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
                }
            }
        }
        return resp;
    }

    private String sendPn(NotifyTemplateDto notifyTemplateDto, Long uid, String templateCode, Map<String, Object> params) {
        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.PN)) {
            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
        }

        UserPnNoticeDO pnNotice = UserPnNoticeDO.builder()
                .pnIcon(notifyTemplateDto.getNotifyPn().getPnIcon())
                .pnRedirect(notifyTemplateDto.getNotifyPn().getPnRedirect())
                .pnBanner(notifyTemplateDto.getNotifyPn().getPnBanner())
                .pnTitle(notifyTemplateDto.getNotifyPn().getPnTitle())
                .pnContent(notifyTemplateDto.getNotifyPn().getPnContent())
                .build();
        pnNotice.setTemplateCode(templateCode)
                .setMsgId("UP" + seqClient.getSegmentId("user-pn"))
                .setUid(uid)
                .setSendStatus(MessageSendStatusEnum.SENDING)
                .setSendTime(LocalDateTime.now())
                .setRetrySendTimes(0)
                .setCreator("admin")
                .setModifier("admin");
        pnNotice.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
        doSendMsg(pnTopic, pnNotice);
        return pnNotice.getMsgId();
    }

    private String sendAr(NotifyTemplateDto notifyTemplateDto, Long uid, String templateCode, Map<String, Object> params) {
        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.AR)) {
            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
        }

        UserArNoticeDO arNotice = UserArNoticeDO.builder()
                .arType(notifyTemplateDto.getNotifyAr().getArType())
                .arTitle(notifyTemplateDto.getNotifyAr().getArTitle())
                .arBanner(notifyTemplateDto.getNotifyAr().getArBanner())
                .arRedirectLabel(notifyTemplateDto.getNotifyAr().getArRedirectLabel())
                .arRedirect(notifyTemplateDto.getNotifyAr().getArRedirect())
                .arContent(notifyTemplateDto.getNotifyAr().getArContent())
                .build();
        arNotice.setTemplateCode(templateCode)
                .setMsgId("UA" + seqClient.getSegmentId("user-ar"))
                .setUid(uid)
                .setSendStatus(MessageSendStatusEnum.SENDING)
                .setSendTime(LocalDateTime.now())
                .setRetrySendTimes(0)
                .setCreator("admin")
                .setModifier("admin");
        arNotice.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
        doSendMsg(arTopic, arNotice);
        return arNotice.getMsgId();
    }


    private String sendMail(NotifyTemplateDto notifyTemplateDto, Long uid, String emailRecipient1, String templateCode, Map<String, Object> params) {
        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.MAIL)) {
            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
        }

        String emailRecipient = StrUtils.conditionStr(NotifyRecipientTypeEnum.SPECIAL_USER == notifyTemplateDto.getRecipientType(),
                emailRecipient1,
                notifyTemplateDto.getNotifyEmail().getEmailRecipient());
        UserEmailMessageDO email = UserEmailMessageDO.builder()
                .fromAddress(notifyTemplateDto.getNotifyEmail().getFromAddress())
                .emailRecipient(emailRecipient)
                .emailContent(notifyTemplateDto.getNotifyEmail().getEmailContent())
                .emailSubject(notifyTemplateDto.getNotifyEmail().getEmailSubject())
                .build();
        email.setTemplateCode(templateCode)
                .setMsgId("UE" + seqClient.getSegmentId("user-email"))
                .setUid(uid)
                .setSendStatus(MessageSendStatusEnum.SENDING)
                .setSendTime(LocalDateTime.now())
                .setRetrySendTimes(0)
                .setCreator("admin")
                .setModifier("admin");
        email.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
        doSendMsg(emailTopic, email);
        return email.getMsgId();
    }

    private String sendSms(NotifyTemplateDto notifyTemplateDto, String smsRecipient1, String templateCode, Map<String, Object> params) {
        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.SMS)) {
            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
        }

        String smsRecipient = StrUtils.conditionStr(NotifyRecipientTypeEnum.SPECIAL_USER == notifyTemplateDto.getRecipientType(),
                smsRecipient1,
                notifyTemplateDto.getNotifySms().getSmsRecipient());
        UserSmsMessageDO sms = UserSmsMessageDO.builder()
                .smsRecipient(smsRecipient)
                .smsContent(notifyTemplateDto.getNotifySms().getSmsContent())
                .build();
        sms.setTemplateCode(templateCode)
                .setMsgId("US" + seqClient.getSegmentId("user-sms"))
                .setSendStatus(MessageSendStatusEnum.SENDING)
                .setSendTime(LocalDateTime.now())
                .setRetrySendTimes(0)
                .setCreator("admin")
                .setModifier("admin");
        sms.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
        doSendMsg(smsTopic, sms);
        return sms.getMsgId();
    }

    private void doSendMsg(String topic, Object msgPayLoad) {
        rocketMQTemplate.asyncSend(topic, JacksonUtils.toJSON(msgPayLoad), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {
                log.error("Message -> RocketMQ error", throwable);
            }
        });
    }
}
