//package com.roaker.notes.uc.consumer.notify;
//
//import com.roaker.notes.notify.api.enums.MessageSendStatusEnum;
//import com.roaker.notes.notify.api.enums.NotifyCommonConstants;
//import com.roaker.notes.notify.dal.dataobject.UserArNoticeDO;
//import com.roaker.notes.notify.dal.mapper.UserArNoticeMapper;
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
//@RocketMQMessageListener(nameServer = "${rocket.name-server}", topic = "${" + NotifyCommonConstants.AR_TOPIC + "}", consumerGroup = "${" + NotifyCommonConstants.KEY_CONSUMER_GROUP + "}")
//@RequiredArgsConstructor
//public class ArMessageConsumer extends AbstractMessageMQListener<UserArNoticeDO> {
//    private final UserArNoticeMapper userArNoticeMapper;
//
//    @Override
//    protected Logger log() {
//        return log;
//    }
//
//    @Override
//    protected String logPrefix() {
//        return "【AR Message Consumer】";
//    }
//
//    @Override
//    protected void doHandleMessage(UserArNoticeDO msg) {
//        //模拟发消息
//        msg.setSendStatus(MessageSendStatusEnum.SEND_SUCCESS);
//        userArNoticeMapper.insert(msg);
//    }
//}
