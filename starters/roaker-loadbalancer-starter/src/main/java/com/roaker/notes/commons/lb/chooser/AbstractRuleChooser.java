package com.roaker.notes.commons.lb.chooser;

import com.roaker.notes.enums.CommonConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
public abstract class AbstractRuleChooser implements IRuleChooser{
    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return null;
        }

        if (instances.size() == 1) {
            return instances.get(0);
        }

        return doSelect(instances);
    }

    protected abstract ServiceInstance doSelect(List<ServiceInstance> instances);

    /**
     * Get the weight of the instance which takes warmup time into account
     * if the uptime is within the warmup time, the weight will be reduce proportionally
     *
     * @param instance 服务实例
     * @return 权重
     */
    protected int getWeight(ServiceInstance instance) {
        return Integer.parseInt(instance.getMetadata().getOrDefault(CommonConstant.WEIGHT_KEY, CommonConstant.DEFAULT_WEIGHT));
    }
}
