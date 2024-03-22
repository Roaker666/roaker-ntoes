package com.roaker.notes.starters.sms.core.client.impl;

import cn.hutool.core.lang.Assert;
import com.roaker.notes.starters.sms.core.client.SmsClient;
import com.roaker.notes.starters.sms.core.client.SmsClientFactory;
import com.roaker.notes.starters.sms.core.client.impl.aliyun.AliyunSmsClient;
import com.roaker.notes.starters.sms.core.client.impl.debug.DebugDingTalkSmsClient;
import com.roaker.notes.starters.sms.core.client.impl.tencent.TencentSmsClient;
import com.roaker.notes.starters.sms.core.enums.SmsChannelEnum;
import com.roaker.notes.starters.sms.core.property.SmsChannelProperties;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lei.rao
 * @since 1.0
 */
@Validated
@Slf4j
@ToString
public class SmsClientFactoryImpl implements SmsClientFactory {

    private final ConcurrentMap<Long, AbstractSmsClient> channelIdClients = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, AbstractSmsClient> channelCodeClients= new ConcurrentHashMap<>();


    public SmsClientFactoryImpl() {
        // 初始化 channelCodeClients 集合
        Arrays.stream(SmsChannelEnum.values()).forEach(channel -> {
            // 创建一个空的 SmsChannelProperties 对象
            SmsChannelProperties properties = new SmsChannelProperties().setCode(channel.getCode())
                    .setApiKey("default default").setApiSecret("default");
            // 创建 Sms 客户端
            AbstractSmsClient smsClient = createSmsClient(properties);
            channelCodeClients.put(channel.getCode(), smsClient);
        });
    }
    @Override
    public SmsClient getSmsClient(Long channelId) {
        return channelIdClients.get(channelId);
    }

    @Override
    public SmsClient getSmsClient(Integer channelCode) {
        return channelCodeClients.get(channelCode);
    }

    @Override
    public SmsClient createOrUpdateSmsClient(SmsChannelProperties properties) {
        AbstractSmsClient client = channelIdClients.get(properties.getId());
        if (client == null) {
            client = this.createSmsClient(properties);
            client.init();
            channelIdClients.put(client.getId(), client);
        } else {
            client.refresh(properties);
        }
        return client;
    }


    private AbstractSmsClient createSmsClient(SmsChannelProperties properties) {
        SmsChannelEnum channelEnum = SmsChannelEnum.getByCode(properties.getCode());
        Assert.notNull(channelEnum, String.format("渠道类型(%s) 为空", channelEnum));

        return switch (channelEnum) {
            case ALIYUN -> new AliyunSmsClient(properties);
            case DEBUG_DING_TALK -> new DebugDingTalkSmsClient(properties);
            case TENCENT -> new TencentSmsClient(properties);
        };
        // 创建失败，错误日志 + 抛出异常
    }
}
