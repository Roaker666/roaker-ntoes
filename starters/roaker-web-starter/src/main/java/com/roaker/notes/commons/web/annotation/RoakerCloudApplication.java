package com.roaker.notes.commons.web.annotation;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.roaker.notes.commons.lb.annotation.EnableFeignInterceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication(scanBasePackages = "com.roaker.notes")
@EnableDiscoveryClient
@EnableKnife4j
@EnableFeignInterceptor
@EnableFeignClients(basePackages = "com.roaker.notes")
//@EnableApolloConfig(value = {"application.yml","application-db.yml","application-log.yml","application-redis.yml","application-web.yml","application-lb.yml"})
public @interface RoakerCloudApplication {

}
