package com.roaker.notes.uc.consumer.notify;

import com.roaker.notes.uc.dal.dataobject.notify.UserPnNoticeDO;
import com.roaker.notes.uc.dal.mapper.notify.UserPnNoticeMapper;
import com.roaker.notes.uc.enums.notify.MessageSendStatusEnum;
import com.roaker.notes.uc.enums.notify.NotifyCommonConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.common.schema.SchemaType;
import org.slf4j.Logger;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;


/**
 * @author lei.rao
 * @since 1.0
 */
@Component
@Slf4j
@PulsarListener(topics = "${" + NotifyCommonConstants.PN_TOPIC + "}", schemaType = SchemaType.JSON, subscriptionName = "${" + NotifyCommonConstants.KEY_CONSUMER_GROUP + "}")
@RequiredArgsConstructor
public class PnMessageConsumer extends AbstractMessageMQListener<UserPnNoticeDO> {
    private final UserPnNoticeMapper userPnNoticeMapper;
    @Override
    protected Logger log() {
        return log;
    }

    @Override
    protected String logPrefix() {
        return "【PN Message Consumer】";
    }

    @Override
    protected void doHandleMessage(UserPnNoticeDO msg) {
        //模拟发消息
        msg.setSendStatus(MessageSendStatusEnum.SEND_SUCCESS);
        userPnNoticeMapper.insert(msg);
    }
}
