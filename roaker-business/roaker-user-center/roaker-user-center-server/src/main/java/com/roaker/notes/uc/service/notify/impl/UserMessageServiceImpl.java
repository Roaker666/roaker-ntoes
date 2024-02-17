//package com.roaker.notes.uc.service.notify.impl;
//
//import com.roaker.notes.commons.constants.ErrorCodeConstants;
//import com.roaker.notes.commons.utils.JacksonUtils;
//import com.roaker.notes.commons.utils.StrUtils;
//import com.roaker.notes.enums.NotifyChannelEnum;
//import com.roaker.notes.enums.NotifyRecipientTypeEnum;
//import com.roaker.notes.uc.api.seq.SeqApi;
//import com.roaker.notes.uc.common.notify.FreemarkerReplaceUtils;
//import com.roaker.notes.uc.dal.dataobject.notify.UserArNoticeDO;
//import com.roaker.notes.uc.dal.dataobject.notify.UserEmailMessageDO;
//import com.roaker.notes.uc.dal.dataobject.notify.UserPnNoticeDO;
//import com.roaker.notes.uc.dal.dataobject.notify.UserSmsMessageDO;
//import com.roaker.notes.uc.dto.notify.NotifyTemplateDto;
//import com.roaker.notes.uc.enums.notify.MessageSendStatusEnum;
//import com.roaker.notes.uc.enums.notify.NotifyCommonConstants;
//import com.roaker.notes.uc.service.notify.NotifyTemplateService;
//import com.roaker.notes.uc.service.notify.UserMessageService;
//import com.roaker.notes.uc.vo.notify.MessageSendReq;
//import com.roaker.notes.uc.vo.notify.MessageSendResp;
//import com.roaker.notes.uc.vo.notify.NotifyResp;
//import com.roaker.notes.uc.vo.notify.SmsSendReq;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;
//
///**
// * @author lei.rao
// * @since 1.0
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UserMessageServiceImpl implements UserMessageService {
//    private final NotifyTemplateService notifyTemplateService;
//    private final RocketMQTemplate rocketMQTemplate;
//    @Value("${" + NotifyCommonConstants.SMS_TOPIC + "}")
//    private String smsTopic;
//    @Value("${" + NotifyCommonConstants.MAIL_TOPIC + "}")
//    private String emailTopic;
//    @Value("${" + NotifyCommonConstants.AR_TOPIC + "}")
//    private String arTopic;
//    @Value("${" + NotifyCommonConstants.PN_TOPIC + "}")
//    private String pnTopic;
//    private final SeqApi seqApi;
//
//
//    @Override
//    public NotifyResp sendSms(SmsSendReq smsSendReq) {
//        NotifyTemplateDto notifyTemplateDto = notifyTemplateService.getNotifyTemplate(smsSendReq.getTemplateCode());
//        if (notifyTemplateDto == null) {
//            throw exception(ErrorCodeConstants.MSG_TEMPLATE_NOT_EXISTS);
//        }
//        return new NotifyResp(sendSms(notifyTemplateDto, smsSendReq.getSmsRecipient(), smsSendReq.getTemplateCode(), smsSendReq.getParams()));
//    }
//
//
//    @Override
//    public MessageSendResp sendMessage(MessageSendReq sendReq) {
//        NotifyTemplateDto notifyTemplateDto = notifyTemplateService.getNotifyTemplate(sendReq.getTemplateCode());
//        if (notifyTemplateDto == null) {
//            throw exception(ErrorCodeConstants.MSG_TEMPLATE_NOT_EXISTS);
//        }
//        MessageSendResp resp = new MessageSendResp();
//
//        if (CollectionUtils.isNotEmpty(notifyTemplateDto.getEnableChannelStatus())) {
//            for (NotifyChannelEnum notifyChannel : notifyTemplateDto.getEnableChannelStatus()) {
//                switch (notifyChannel) {
//                    case SMS ->
//                            resp.setSmsMsgId(sendSms(notifyTemplateDto, sendReq.getSmsRecipient(), sendReq.getTemplateCode(), sendReq.getParams()));
//                    case MAIL ->
//                            resp.setEmailMsgId(sendMail(notifyTemplateDto, sendReq.getUid(), sendReq.getEmailRecipient(), sendReq.getTemplateCode(), sendReq.getParams()));
//                    case AR ->
//                            resp.setArMsgId(sendAr(notifyTemplateDto, sendReq.getUid(), sendReq.getTemplateCode(), sendReq.getParams()));
//                    case PN ->
//                            resp.setPnMsgId(sendPn(notifyTemplateDto, sendReq.getUid(), sendReq.getTemplateCode(), sendReq.getParams()));
//                    default -> throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
//                }
//            }
//        }
//        return resp;
//    }
//
//    private String sendPn(NotifyTemplateDto notifyTemplateDto, String uid, String templateCode, Map<String, Object> params) {
//        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.PN)) {
//            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
//        }
//
//        UserPnNoticeDO pnNotice = UserPnNoticeDO.builder()
//                .pnIcon(notifyTemplateDto.getNotifyPn().getPnIcon())
//                .pnRedirect(notifyTemplateDto.getNotifyPn().getPnRedirect())
//                .pnBanner(notifyTemplateDto.getNotifyPn().getPnBanner())
//                .pnTitle(notifyTemplateDto.getNotifyPn().getPnTitle())
//                .pnContent(notifyTemplateDto.getNotifyPn().getPnContent())
//                .build();
//        pnNotice.setTemplateCode(templateCode)
//                .setMsgId("UP" + seqApi.getSegmentId("user-pn"))
//                .setUid(uid)
//                .setSendStatus(MessageSendStatusEnum.SENDING)
//                .setSendTime(LocalDateTime.now())
//                .setRetrySendTimes(0)
//                .setCreator("admin")
//                .setModifier("admin");
//        pnNotice.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
//        doSendMsg(pnTopic, pnNotice);
//        return pnNotice.getMsgId();
//    }
//
//    private String sendAr(NotifyTemplateDto notifyTemplateDto, String uid, String templateCode, Map<String, Object> params) {
//        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.AR)) {
//            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
//        }
//
//        UserArNoticeDO arNotice = UserArNoticeDO.builder()
//                .arType(notifyTemplateDto.getNotifyAr().getArType())
//                .arTitle(notifyTemplateDto.getNotifyAr().getArTitle())
//                .arBanner(notifyTemplateDto.getNotifyAr().getArBanner())
//                .arRedirectLabel(notifyTemplateDto.getNotifyAr().getArRedirectLabel())
//                .arRedirect(notifyTemplateDto.getNotifyAr().getArRedirect())
//                .arContent(notifyTemplateDto.getNotifyAr().getArContent())
//                .build();
//        arNotice.setTemplateCode(templateCode)
//                .setMsgId("UA" + seqApi.getSegmentId("user-ar"))
//                .setUid(uid)
//                .setSendStatus(MessageSendStatusEnum.SENDING)
//                .setSendTime(LocalDateTime.now())
//                .setRetrySendTimes(0)
//                .setCreator("admin")
//                .setModifier("admin");
//        arNotice.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
//        doSendMsg(arTopic, arNotice);
//        return arNotice.getMsgId();
//    }
//
//
//    private String sendMail(NotifyTemplateDto notifyTemplateDto, String uid, String emailRecipient1, String templateCode, Map<String, Object> params) {
//        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.MAIL)) {
//            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
//        }
//
//        String emailRecipient = StrUtils.conditionStr(NotifyRecipientTypeEnum.SPECIAL_USER == notifyTemplateDto.getRecipientType(),
//                emailRecipient1,
//                notifyTemplateDto.getNotifyEmail().getEmailRecipient());
//        UserEmailMessageDO email = UserEmailMessageDO.builder()
//                .fromAddress(notifyTemplateDto.getNotifyEmail().getFromAddress())
//                .emailRecipient(emailRecipient)
//                .emailContent(notifyTemplateDto.getNotifyEmail().getEmailContent())
//                .emailSubject(notifyTemplateDto.getNotifyEmail().getEmailSubject())
//                .build();
//        email.setTemplateCode(templateCode)
//                .setMsgId("UE" + seqApi.getSegmentId("user-email"))
//                .setUid(uid)
//                .setSendStatus(MessageSendStatusEnum.SENDING)
//                .setSendTime(LocalDateTime.now())
//                .setRetrySendTimes(0)
//                .setCreator("admin")
//                .setModifier("admin");
//        email.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
//        doSendMsg(emailTopic, email);
//        return email.getMsgId();
//    }
//
//    private String sendSms(NotifyTemplateDto notifyTemplateDto, String smsRecipient1, String templateCode, Map<String, Object> params) {
//        if (!CollectionUtils.containsAny(notifyTemplateDto.getEnableChannelStatus(), NotifyChannelEnum.SMS)) {
//            throw exception(ErrorCodeConstants.MSG_CHANNEL_NOT_SUPPORT);
//        }
//
//        String smsRecipient = StrUtils.conditionStr(NotifyRecipientTypeEnum.SPECIAL_USER == notifyTemplateDto.getRecipientType(),
//                smsRecipient1,
//                notifyTemplateDto.getNotifySms().getSmsRecipient());
//        UserSmsMessageDO sms = UserSmsMessageDO.builder()
//                .smsRecipient(smsRecipient)
//                .smsContent(notifyTemplateDto.getNotifySms().getSmsContent())
//                .build();
//        sms.setTemplateCode(templateCode)
//                .setMsgId("US" + seqApi.getSegmentId("user-sms"))
//                .setSendStatus(MessageSendStatusEnum.SENDING)
//                .setSendTime(LocalDateTime.now())
//                .setRetrySendTimes(0)
//                .setCreator("admin")
//                .setModifier("admin");
//        sms.replaceContent(smsContent -> FreemarkerReplaceUtils.wrapAndReplace(smsContent, params));
//        doSendMsg(smsTopic, sms);
//        return sms.getMsgId();
//    }
//
//    private void doSendMsg(String topic, Object msgPayLoad) {
//        rocketMQTemplate.asyncSend(topic, JacksonUtils.toJSON(msgPayLoad), new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                log.error("Message -> RocketMQ error", throwable);
//            }
//        });
//    }
//}
