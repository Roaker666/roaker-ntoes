package com.roaker.notes.file.config;

import com.roaker.notes.file.core.client.FileClientFactory;
import com.roaker.notes.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei.rao
 * @since 1.0
 */
@Configuration
public class RoakerFileAutoConfiguration {
    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
