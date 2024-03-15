package com.roaker.notes.uc.consumer.notify;

import com.roaker.notes.file.core.utils.FileTypeUtils;
import com.roaker.notes.uc.api.encrypt.FileApi;
import com.roaker.notes.uc.dal.dataobject.notify.UserEmailMessageDO;
import com.roaker.notes.uc.dal.mapper.notify.UserEmailMessageMapper;
import com.roaker.notes.uc.dto.notify.EmailAttachmentFile;
import com.roaker.notes.uc.enums.notify.MessageSendStatusEnum;
import com.roaker.notes.uc.enums.notify.NotifyCommonConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.pulsar.common.schema.SchemaType;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;


/**
 * @author lei.rao
 * @since 1.0
 */
@Component
@Slf4j
@PulsarListener(topics = "${" + NotifyCommonConstants.MAIL_TOPIC + "}", schemaType = SchemaType.JSON, subscriptionName = "${" + NotifyCommonConstants.KEY_CONSUMER_GROUP + "}")
@RequiredArgsConstructor
public class UserEmailConsumer extends AbstractMessageMQListener<UserEmailMessageDO> {

    private final UserEmailMessageMapper userEmailMessageMapper;
    private final JavaMailSender javaMailSender;
    private final FileApi fileApi;

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    protected String logPrefix() {
        return "【EMAIL Message Consumer】";
    }

    @Override
    protected void doHandleMessage(UserEmailMessageDO msg) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper help = new MimeMessageHelper(mimeMessage, true);
            help.setFrom(msg.getFromAddress());
            help.setTo(msg.getEmailRecipient());
            help.setSubject(msg.getEmailSubject());
            help.setText(msg.getEmailContent());
            if (CollectionUtils.isNotEmpty(msg.getAttachmentFileList())) {
                for (EmailAttachmentFile attachmentFile : msg.getAttachmentFileList()) {
                    byte[] data = fileApi.downloadFile(attachmentFile.getFileUrl());
                    help.addAttachment(attachmentFile.getFileName(), new ByteArrayDataSource(data, FileTypeUtils.getMineType(data)));
                }
            }
            javaMailSender.send(help.getMimeMessage());
            msg.setSendStatus(MessageSendStatusEnum.SEND_SUCCESS);
        } catch (MessagingException e) {
            log.error("{}send error", logPrefix(), e);
            msg.setSendStatus(MessageSendStatusEnum.SEND_FAILURE);
        }
        userEmailMessageMapper.insert(msg);
    }
}
