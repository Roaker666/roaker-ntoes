package com.roaker.notes.starters.sms.config;

import com.roaker.notes.starters.sms.core.client.SmsClientFactory;
import com.roaker.notes.starters.sms.core.client.impl.SmsClientFactoryImpl;
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
        return new SmsClientFactoryImpl();
    }

}
