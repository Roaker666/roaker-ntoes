package com.roaker.notes.pay.config;

import com.roaker.notes.pay.core.client.PayClientFactory;
import com.roaker.notes.pay.core.client.impl.PayClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei.rao
 * @since 1.0
 */
@Configuration
public class RoakerPayAutoConfiguration {
    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
