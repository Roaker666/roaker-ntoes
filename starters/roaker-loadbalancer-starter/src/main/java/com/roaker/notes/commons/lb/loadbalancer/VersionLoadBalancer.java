package com.roaker.notes.commons.lb.loadbalancer;

import com.roaker.notes.commons.lb.chooser.IRuleChooser;
import com.roaker.notes.commons.lb.utils.QueryUtils;
import com.roaker.notes.enums.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class VersionLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private final static String KEY_DEFAULT = "default";

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;

    private final String serviceId;

    private final IRuleChooser iRuleChooser;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        //从request从获取版本，兼容webflux
        RequestData requestData = ((RequestDataContext) (request.getContext())).getClientRequest();
        String version = getVersionFromRequestData(requestData);
        log.debug("选择的版本号为:{}", version);
        return Objects.requireNonNull(serviceInstanceListSuppliers.getIfAvailable()).get(request).next().map(instances -> getInstanceResponse(instances, version));
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, String version) {
        List<ServiceInstance> filteredServiceInstanceList = instances;
        if (StringUtils.isNotEmpty(version)) {
            if (CollectionUtils.isNotEmpty(instances)) {
                filteredServiceInstanceList = instances.stream()
                        .filter(item -> item.getMetadata().containsKey(CommonConstant.METADATA_VERSION) &&
                                version.equals(item.getMetadata().get(CommonConstant.METADATA_VERSION)))
                        .collect(Collectors.toList());
            }
        }
        // 如果没有找到对应的版本实例时，选择版本号为空的或这版本为default的实例
        if (CollectionUtils.isEmpty(filteredServiceInstanceList)) {
            filteredServiceInstanceList = instances.stream()
                    .filter(item -> !item.getMetadata().containsKey(CommonConstant.METADATA_VERSION) ||
                            StringUtils.isBlank(item.getMetadata().get(CommonConstant.METADATA_VERSION))
                            || "default".equals(item.getMetadata().get(CommonConstant.METADATA_VERSION)))
                    .collect(Collectors.toList());
        }
        //经过两轮过滤后如果能找到的话就选择，不然返回空
        if (CollectionUtils.isNotEmpty(filteredServiceInstanceList)) {
            ServiceInstance serviceInstance = this.iRuleChooser.choose(filteredServiceInstanceList);
            if (!Objects.isNull(serviceInstance)) {
                log.debug("使用serviceId为：{}服务， 选择version为：{}， 地址：{}:{}，", serviceId, version
                        , serviceInstance.getHost(), serviceInstance.getPort());
            }
            return new DefaultResponse(serviceInstance);
        }
        return new EmptyResponse();
    }

    /**
     * 1. 先获取到拦截的版本，如果不为空的话就将service列表过滤，寻找metadata中哪个服务是配置的版本，
     * 如果版本为空则不需要进行过滤直接提交给service选择器进行选择
     * 2. 如果没有找到版本对应的实例，则找所有版本为空或者版本号为default的实例
     * 3.将instance列表提交到选择器根据对应的策略返回一个instance
     *
     * @param requestData 请求
     * @return 实例
     */
    private String getVersionFromRequestData(RequestData requestData) {
        Map<String, String> queryMap = QueryUtils.getQueryMap(requestData.getUrl());
        if (MapUtils.isNotEmpty(queryMap) &&
                queryMap.containsKey(CommonConstant.ROAKER_VERSION) &&
                StringUtils.isNotBlank(queryMap.get(CommonConstant.ROAKER_VERSION))) {
            return queryMap.get(CommonConstant.ROAKER_VERSION);
        } else if (requestData.getHeaders().containsKey(CommonConstant.ROAKER_VERSION)) {
            return Objects.requireNonNull(requestData.getHeaders().get(CommonConstant.ROAKER_VERSION)).get(0);
        }
        return null;
    }
}
