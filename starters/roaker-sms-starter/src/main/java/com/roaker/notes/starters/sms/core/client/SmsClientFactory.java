package com.roaker.notes.starters.sms.core.client;

import com.roaker.notes.starters.sms.core.property.SmsChannelProperties;

/**
 * 短信客户端的工厂接口
 *
 * @author lei.rao
 * @since 1.0
 */
public interface SmsClientFactory {
    /**
     * 获得短信 Client
     *
     * @param channelId 渠道编号
     * @return 短信 Client
     */
    SmsClient getSmsClient(Long channelId);

    /**
     * 获得短信 Client
     *
     * @param channelCode 渠道编码
     * @return 短信 Client
     */
    SmsClient getSmsClient(Integer channelCode);

    /**
     * 创建短信 Client
     *
     * @param properties 配置对象
     */
    SmsClient createOrUpdateSmsClient(SmsChannelProperties properties);
}
