package com.roaker.notes.commons.lb.chooser;

import org.springframework.cloud.client.ServiceInstance;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lei.rao
 * @since 1.0
 */
public class RandomRuleChooser extends AbstractRuleChooser {
    @Override
    protected ServiceInstance doSelect(List<ServiceInstance> instances) {
        int length = instances.size();
        //每个服务实例都有相同的weight?
        boolean sameWeight = true;
        //累计权重,也就是每个服务实例的最大权重位置，最小为0，最大为最后一个服务实例的可以达到的权重位置
        int[] weights = new int[length];
        int totalWeight = 0;
        for (int i = 0; i < length; i++) {
            int weight = getWeight(instances.get(i));
            totalWeight += weight;
            weights[i] = totalWeight;
            //某个服务实例权重不相同
            if (sameWeight && totalWeight != weight * (i + 1)) {
                sameWeight = false;
            }
        }
        //服务实例权重不同,那就基于总权重随机选择一个
        if (totalWeight > 0 && !sameWeight) {
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            if (length < 4) {
                for (int i = 0; i < length; i++) {
                    if (offset < weights[i]) {
                        return instances.get(i);
                    }
                }
            } else {
                int i = Arrays.binarySearch(weights, offset);
                //not found,回到最后一次查找的位置，而且该位置刚好小于offset
                if (i < 0) {
                    i = -(i + 1);
                } else {
                    //往后遍历到刚好大于offset的位置
                    while (weights[i + 1] == offset) {
                        i++;
                    }
                    i++;
                }
                return instances.get(i);
            }
        }

        return instances.get(ThreadLocalRandom.current().nextInt(length));
    }


    @Override
    public String getName() {
        return "random";
    }
}
