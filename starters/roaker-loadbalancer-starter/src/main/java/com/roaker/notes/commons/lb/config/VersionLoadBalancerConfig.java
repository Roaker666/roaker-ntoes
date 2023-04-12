package com.roaker.notes.commons.lb.config;

import com.roaker.notes.commons.lb.chooser.IRuleChooser;
import com.roaker.notes.commons.lb.chooser.RoundRobinRuleChooser;
import com.roaker.notes.commons.lb.loadbalancer.VersionLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.ServiceLoader;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class VersionLoadBalancerConfig {
    public static final String CONFIG_LOADBALANCE_ISOLATION = "roaker.loadbalance.isolation";

    public static final String CONFIG_LOADBALANCE_ISOLATION_ENABLE = CONFIG_LOADBALANCE_ISOLATION + ".enabled";

    public static final String CONFIG_LOADBALANCE_ISOLATION_CHOOSER = CONFIG_LOADBALANCE_ISOLATION + ".chooser";

    public static final String CONFIG_LOADBALANCE_VERSION = "roaker.loadbalance.version";

    private IRuleChooser defaultRuleChooser = null;

    @Bean
    @ConditionalOnMissingBean(IRuleChooser.class)
    @ConditionalOnProperty(prefix = CONFIG_LOADBALANCE_ISOLATION, havingValue = "chooser")
    public IRuleChooser customerRuleChooser(Environment environment, ApplicationContext context) {
        ServiceLoader<IRuleChooser> iRuleChoosers = ServiceLoader.load(IRuleChooser.class);
        if (environment.containsProperty(CONFIG_LOADBALANCE_ISOLATION_CHOOSER)) {
            String name = environment.getProperty(CONFIG_LOADBALANCE_ISOLATION_CHOOSER);
            for (IRuleChooser iruleChooser : iRuleChoosers) {
                if (StringUtils.equalsIgnoreCase(name, iruleChooser.getName())) {
                    return iruleChooser;
                }
            }
        }
        return new RoundRobinRuleChooser();
    }

    @Bean
    @ConditionalOnMissingBean(IRuleChooser.class)
    public IRuleChooser defaultRuleChooser() {
        return new RoundRobinRuleChooser();
    }

    @Bean
    @ConditionalOnProperty(prefix = CONFIG_LOADBALANCE_ISOLATION, name = "enabled", havingValue = "true", matchIfMissing = false)
    public ReactorServiceInstanceLoadBalancer versionServiceLoanBalancer(Environment environment,
                                                                         LoadBalancerClientFactory loadBalancerClientFactory,
                                                                         IRuleChooser iRuleChooser) {
        String clientName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new VersionLoadBalancer(loadBalancerClientFactory.getLazyProvider(clientName, ServiceInstanceListSupplier.class), clientName, defaultRuleChooser);
    }

}
