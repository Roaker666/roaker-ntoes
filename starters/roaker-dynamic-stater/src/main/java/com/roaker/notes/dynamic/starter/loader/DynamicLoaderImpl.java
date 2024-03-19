package com.roaker.notes.dynamic.starter.loader;

import cn.hutool.core.collection.CollUtil;
import com.roaker.notes.commons.utils.date.DateUtils;
import com.roaker.notes.dynamic.enums.DynamicDictConfigLoader;
import com.roaker.notes.dynamic.enums.DynamicDictDO;
import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import com.roaker.notes.uc.api.dict.DynamicApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class DynamicLoaderImpl implements DynamicLoader {
    private static final int REFRESH_PERIOD = 60 * 1000;

    private final String applicationName;
    private final DynamicApi dynamicApi;
    private LocalDateTime maxUpdateTime;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDynamicDict() {
        loadDynamicDict0();
    }

    @Scheduled(fixedDelay = REFRESH_PERIOD, initialDelay = REFRESH_PERIOD)
    public void refreshDynamicDict() {
        loadDynamicDict0();
    }

    private void loadDynamicDict0() {
        // 加载错误码
        List<DynamicDictDO> dynamicDictList = dynamicApi.loadDataList(maxUpdateTime);
        if (CollUtil.isEmpty(dynamicDictList)) {
            return;
        }
        log.info("[loadDynamicDict0][加载到 ({}) 个动态参数]", dynamicDictList.size());
        // 刷新错误码的缓存
        dynamicDictList.forEach(dynamicDictDO -> {
            if (DynamicDictTypeEnums.ERROR_CODE.getCode().equals(dynamicDictDO.getCode()) &&
                    StringUtils.equalsIgnoreCase(applicationName, dynamicDictDO.getBizName())) {
                putErrorCode(dynamicDictDO.getCode(), dynamicDictDO.getName());
            }
            maxUpdateTime = DateUtils.max(maxUpdateTime, dynamicDictDO.getUpdateTime());
        });
        DynamicDictConfigLoader.initEnumClass(dynamicDictList);
    }
}
