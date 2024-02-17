//package com.roaker.notes.uc.consumer.notify;
//
//import com.roaker.notes.notify.api.enums.MessageSendStatusEnum;
//import com.roaker.notes.notify.api.enums.NotifyCommonConstants;
//import com.roaker.notes.notify.dal.dataobject.UserEmailMessageDO;
//import com.roaker.notes.notify.dal.mapper.UserEmailMessageMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.slf4j.Logger;
//import org.springframework.stereotype.Component;
//
//import static com.roaker.notes.notify.api.enums.NotifyCommonConstants.KEY_CONSUMER_GROUP;
//
///**
// * @author lei.rao
// * @since 1.0
// */
//@Component
//@Slf4j
//@RocketMQMessageListener(nameServer = "${rocket.name-server}", topic = "${" + NotifyCommonConstants.SMS_TOPIC + "}", consumerGroup = "${" + NotifyCommonConstants.KEY_CONSUMER_GROUP + "}")
//@RequiredArgsConstructor
//public class UserEmailConsumer extends AbstractMessageMQListener<UserEmailMessageDO>{
//
//    private final UserEmailMessageMapper userEmailMessageMapper;
//    @Override
//    protected Logger log() {
//        return log;
//    }
//
//    @Override
//    protected String logPrefix() {
//        return "【EMAIL Message Consumer】";
//    }
//
//    @Override
//    protected void doHandleMessage(UserEmailMessageDO msg) {
//        //模拟发消息
//        msg.setSendStatus(MessageSendStatusEnum.SEND_SUCCESS);
//        userEmailMessageMapper.insert(msg);
//    }
//}
