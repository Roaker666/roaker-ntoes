package com.roaker.notes.commons.lb.chooser;

import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lei.rao
 * @since 1.0
 */
public class RoundRobinRuleChooser extends AbstractRuleChooser {
    private static final int RECYCLE_PERIOD = 60000;
    private final ConcurrentMap<String, ConcurrentMap<String, WeightedRoundRobin>> methodWeightMap = new ConcurrentHashMap<>();

    @Override
    public ServiceInstance doSelect(List<ServiceInstance> instances) {
        String serviceId = instances.get(0).getServiceId();
        ConcurrentMap<String, WeightedRoundRobin> map = methodWeightMap.computeIfAbsent(serviceId, key -> new ConcurrentHashMap<>());

        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = System.currentTimeMillis();
        ServiceInstance selectedInstance = null;
        WeightedRoundRobin selectedWRR = null;
        for (ServiceInstance instance : instances) {
            int weight = getWeight(instance);
            WeightedRoundRobin weightedRoundRobin = map.computeIfAbsent(instance.getInstanceId(), key -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(weight);
                return wrr;
            });

            if (weight != weightedRoundRobin.getWeight()) {
                //第二次选择后，已存在的instance的权重可能会发生改变
                weightedRoundRobin.setWeight(weight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(now);
            if (cur > maxCurrent) {
                maxCurrent = cur;
                selectedInstance = instance;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;
        }

        if (instances.size() != map.size()) {
            map.entrySet().removeIf(item -> now - item.getValue().getLastUpdate() > RECYCLE_PERIOD);
        }

        if (selectedInstance != null) {
            selectedWRR.sel(totalWeight);
            return selectedInstance;
        }
        return instances.get(0);
    }

    @Override
    public String getName() {
        return "roundRobin";
    }

    @Data
    protected static class WeightedRoundRobin {
        private int weight;
        private final AtomicLong current = new AtomicLong(0);
        private long lastUpdate;

        public void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }
    }
}
