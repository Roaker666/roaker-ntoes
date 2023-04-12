package com.roaker.notes.commons.web.annotation;

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
public @interface RoakerCloudApplication {
}
