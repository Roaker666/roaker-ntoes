package com.roaker.notes.dynamic.enums;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class DynamicDictConfigLoader {
    public static synchronized  void initEnumClass(List<DynamicDictDO> dictDOList) {
        if (CollectionUtils.isEmpty(dictDOList)) {
            log.info("new config is empty,refresh DynamicDict enum end");
            return;
        }
        // 1.对查询的数据按照类名进行分组
        Map<String, List<DynamicDictDO>> groupListDO = dictDOList.stream()
                .collect(Collectors.groupingBy(DynamicDictDO::getBizClass));

        // 2.按照类名分组后去初始化枚举配置
        groupListDO.forEach((k, v) -> {
            try {
                DynamicEnumUtil.addEnumBatch(Class.forName(k), v);
            } catch (ClassNotFoundException e) {
                log.warn("init single dynamic enum class error，type is:{}", k);
            }
        });

    }
}
