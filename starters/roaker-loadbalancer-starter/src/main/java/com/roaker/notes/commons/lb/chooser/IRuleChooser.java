package com.roaker.notes.commons.lb.chooser;


import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface IRuleChooser {
    ServiceInstance choose(List<ServiceInstance> instances);

    String getName();
}
