package com.roaker.notes.uc.config;

import com.roaker.notes.uc.dal.dataobject.notify.UserArNoticeDO;
import com.roaker.notes.uc.dal.dataobject.notify.UserEmailMessageDO;
import com.roaker.notes.uc.dal.dataobject.notify.UserPnNoticeDO;
import com.roaker.notes.uc.dal.dataobject.notify.UserSmsMessageDO;
import org.apache.pulsar.client.api.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.core.DefaultSchemaResolver;
import org.springframework.pulsar.core.SchemaResolver;

/**
 * @author lei.rao
 * @since 1.0
 */
@Configuration
public class PulsarConfig {
    @Bean
    public SchemaResolver.SchemaResolverCustomizer<DefaultSchemaResolver> schemaResolverCustomizer() {
        return (schemaResolver) -> {
            schemaResolver.addCustomSchemaMapping(UserSmsMessageDO.class, Schema.JSON(UserSmsMessageDO.class));
            schemaResolver.addCustomSchemaMapping(UserEmailMessageDO.class, Schema.JSON(UserEmailMessageDO.class));
            schemaResolver.addCustomSchemaMapping(UserArNoticeDO.class, Schema.JSON(UserArNoticeDO.class));
            schemaResolver.addCustomSchemaMapping(UserPnNoticeDO.class, Schema.JSON(UserPnNoticeDO.class));
        };
    }
}
