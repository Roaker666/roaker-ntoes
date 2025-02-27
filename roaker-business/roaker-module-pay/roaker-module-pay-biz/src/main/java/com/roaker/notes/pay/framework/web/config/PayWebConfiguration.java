package com.roaker.notes.pay.framework.web.config;

import com.roaker.notes.commons.web.swagger.config.RoakerSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * pay 模块的 web 组件的 Configuration
 *
 */
@Configuration(proxyBeanMethods = false)
public class PayWebConfiguration {

    /**
     * pay 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi payGroupedOpenApi() {
        return RoakerSwaggerAutoConfiguration.buildGroupedOpenApi("pay");
    }

}
