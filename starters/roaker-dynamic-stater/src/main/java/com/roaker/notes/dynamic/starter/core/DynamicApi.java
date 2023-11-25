package com.roaker.notes.dynamic.starter.core;

import com.roaker.notes.commons.constants.ApplicationNameConstants;
import com.roaker.notes.dynamic.enums.DynamicDictDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@FeignClient(name = ApplicationNameConstants.ENCRYPT_NAME, fallbackFactory = DynamicApiFallback.class, dismiss404 = true)
public interface DynamicApi {
    /**
     * 增量获得数组
     * <p>
     * 如果 minUpdateTime 为空时，则获取所有错误码
     *
     * @param minUpdateTime 最小更新时间
     * @return 错误码数组
     */
    @GetMapping("/infra/dynamic-dict/list")
    List<DynamicDictDO> getDynamicDictList(LocalDateTime minUpdateTime);

}
