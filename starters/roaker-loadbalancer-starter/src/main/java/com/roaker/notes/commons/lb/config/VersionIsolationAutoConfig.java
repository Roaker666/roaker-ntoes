package com.roaker.notes.commons.lb.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Import;

import static com.roaker.notes.commons.lb.config.VersionLoadBalancerConfig.CONFIG_LOADBALANCE_ISOLATION;

/**
 * @author lei.rao
 * @since 1.0
 */
@LoadBalancerClients(defaultConfiguration = VersionLoadBalancerConfig.class)
@ConditionalOnProperty(prefix = CONFIG_LOADBALANCE_ISOLATION, name = "enabled", havingValue = "true", matchIfMissing = false)
@Import({VersionRegisterBeanPostProcessor.class})
public class VersionIsolationAutoConfig {

}
