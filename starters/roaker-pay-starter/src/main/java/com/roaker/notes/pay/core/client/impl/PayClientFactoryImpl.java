package com.roaker.notes.pay.core.client.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.roaker.notes.pay.core.client.PayClient;
import com.roaker.notes.pay.core.client.PayClientConfig;
import com.roaker.notes.pay.core.client.PayClientFactory;
import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 支付客户端的工厂实现类
 *
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class PayClientFactoryImpl implements PayClientFactory {
    /**
     * 支付客户端 Map
     * key：渠道编号
     */
    private final ConcurrentMap<Long, AbstractPayClient<?>> clients = new ConcurrentHashMap<>();
    /**
     * 支付客户端 Class Map
     */
    private final Map<PayChannelEnum, Class<?>> clientClass = new ConcurrentHashMap<>();


    @Override
    public PayClient getPayClient(Long channelId) {
        AbstractPayClient<?> client = clients.get(channelId);
        if (client == null) {
            log.error("[getPayClient][渠道编号({}) 找不到客户端]", channelId);
        }
        return client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Config extends PayClientConfig> PayClient createOrUpdatePayClient(Long channelId,
                                                                              String channelCode,
                                                                              Config config) {
        AbstractPayClient<Config> client = (AbstractPayClient<Config>) clients.get(channelId);
        if (client == null) {
            client = this.createPayClient(channelId, channelCode, config);
            client.init();
            clients.put(client.getId(), client);
        } else {
            client.refresh(config);
        }
        return client;
    }

    @SuppressWarnings("unchecked")
    private <Config extends PayClientConfig> AbstractPayClient<Config> createPayClient(Long channelId,
                                                                                       String channelCode,
                                                                                       Config config) {
        PayChannelEnum channelEnum = PayChannelEnum.getByCode(channelCode);
        Assert.notNull(channelEnum, String.format("支付渠道(%s) 为空", channelCode));
        Class<?> payClientClass = clientClass.get(channelEnum);
        Assert.notNull(payClientClass, String.format("支付渠道(%s) Class 为空", channelCode));
        return (AbstractPayClient<Config>) ReflectUtil.newInstance(payClientClass, channelId, config);
    }

    @Override
    public void registerPayClientClass(PayChannelEnum channel, Class<?> payClientClass) {
        clientClass.put(channel, payClientClass);
    }
}
