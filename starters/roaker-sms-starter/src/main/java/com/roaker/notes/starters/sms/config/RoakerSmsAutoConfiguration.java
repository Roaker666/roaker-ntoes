package com.roaker.notes.starters.sms.config;

import com.roaker.notes.starters.sms.core.client.SmsClientFactory;
import com.roaker.notes.starters.sms.core.client.impl.SmsClientFactoryImpl;
import com.roaker.notes.starters.sms.core.property.SmsChannelProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei.rao
 * @since 1.0
 */
@Configuration
public class RoakerSmsAutoConfiguration {
    @Bean
    public SmsClientFactory smsClientFactory() {
        SmsClientFactoryImpl smsClientFactory = new SmsClientFactoryImpl();
        smsClientFactory.createOrUpdateSmsClient(smsChannelProperties());
        return smsClientFactory;
    }

    @Bean
    @ConfigurationProperties(prefix = "roaker.sms.channel")
    public SmsChannelProperties smsChannelProperties() {
        return new SmsChannelProperties();
    }

}
