//package com.roaker.notes.uc.consumer.notify;
//
//import com.roaker.notes.notify.api.enums.MessageSendStatusEnum;
//import com.roaker.notes.notify.api.enums.NotifyCommonConstants;
//import com.roaker.notes.notify.dal.dataobject.UserSmsMessageDO;
//import com.roaker.notes.notify.dal.mapper.UserSmsMessageMapper;
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
//public class UserSmsConsumer extends AbstractMessageMQListener<UserSmsMessageDO>{
//
//    private final UserSmsMessageMapper userSmsMessageMapper;
//    @Override
//    protected Logger log() {
//        return log;
//    }
//
//    @Override
//    protected String logPrefix() {
//        return "【SMS Message Consumer】";
//    }
//
//    @Override
//    protected void doHandleMessage(UserSmsMessageDO msg) {
//        //模拟发消息
//        msg.setSendStatus(MessageSendStatusEnum.SEND_SUCCESS);
//        userSmsMessageMapper.insert(msg);
//    }
//}
