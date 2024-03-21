package com.roaker.notes.commons.lb.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.roaker.notes.enums.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author lei.rao
 * @since 1.0
 */
@Component
public class VersionRegisterBeanPostProcessor implements BeanPostProcessor {
    @Value("${roaker.loadbalance.version}")
    private String version;

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (bean instanceof NacosDiscoveryProperties nacosDiscoveryProperties && StringUtils.isNotEmpty(version)) {
            nacosDiscoveryProperties.getMetadata().putIfAbsent(CommonConstant.METADATA_VERSION, version);
        }
        return bean;
    }
}
