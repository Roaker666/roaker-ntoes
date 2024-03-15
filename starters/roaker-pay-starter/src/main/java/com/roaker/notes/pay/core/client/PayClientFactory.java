package com.roaker.notes.pay.core.client;

import com.roaker.notes.pay.core.enums.channel.PayChannelEnum;

/**
 * 支付客户端的工厂接口
 *
 * @author lei.rao
 * @since 1.0
 */
public interface PayClientFactory {
    /**
     * 获得支付客户端
     *
     * @param channelId 渠道编号
     * @return 支付客户端
     */
    PayClient getPayClient(Long channelId);

    /**
     * 创建支付客户端
     *
     * @param channelId   渠道编号
     * @param channelCode 渠道编码
     * @param config      支付配置
     */
    <Config extends PayClientConfig> PayClient createOrUpdatePayClient(Long channelId,
                                                                  String channelCode,
                                                                  Config config);


    /**
     * 注册支付客户端 Class，用于模块中实现的 PayClient
     *
     * @param channel 支付渠道的编码的枚举
     * @param payClientClass 支付客户端 class
     */
    void registerPayClientClass(PayChannelEnum channel, Class<?> payClientClass);
}
