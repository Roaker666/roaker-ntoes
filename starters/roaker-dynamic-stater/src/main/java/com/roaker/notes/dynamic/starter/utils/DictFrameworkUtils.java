package com.roaker.notes.dynamic.starter.utils;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.roaker.notes.commons.core.KeyValue;
import com.roaker.notes.commons.utils.CacheUtils;
import com.roaker.notes.dynamic.enums.DynamicDictDO;
import com.roaker.notes.uc.api.dict.DynamicApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class DictFrameworkUtils {

    private static DynamicApi dynamicApi;

    private static final DynamicDictDO DICT_DATA_NULL = new DynamicDictDO();

    /**
     * 针对 {@link #getDictDataLabel(String, String)} 的缓存
     */
    private static final LoadingCache<KeyValue<String, String>, DynamicDictDO> GET_DICT_DATA_CACHE = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(120L), // 过期时间 1 分钟
            new CacheLoader<>() {

                @NotNull
                @Override
                public DynamicDictDO load(@NotNull KeyValue<String, String> key) {
                    return ObjectUtil.defaultIfNull(dynamicApi.getDictData(key.getKey(), key.getValue()), DICT_DATA_NULL);
                }
            });

    /**
     * 针对 {@link #parseDictDataValue(String, String)} 的缓存
     */
    private static final LoadingCache<KeyValue<String, String>, DynamicDictDO> PARSE_DICT_DATA_CACHE = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(120L), // 过期时间 1 分钟
            new CacheLoader<>() {

                @NotNull
                @Override
                public DynamicDictDO load(@NotNull KeyValue<String, String> key) {
                    return ObjectUtil.defaultIfNull(dynamicApi.parseDictData(key.getKey(), key.getValue()), DICT_DATA_NULL);
                }

            });

    public static void init(DynamicApi dictDataApi) {
        DictFrameworkUtils.dynamicApi = dictDataApi;
        log.info("[init][初始化 DictFrameworkUtils 成功]");
    }

    @SneakyThrows
    public static String getDictDataLabel(String dictType, Integer value) {
        return GET_DICT_DATA_CACHE.get(new KeyValue<>(dictType, String.valueOf(value))).getLabel();
    }

    @SneakyThrows
    public static String getDictDataLabel(String dictType, String value) {
        return GET_DICT_DATA_CACHE.get(new KeyValue<>(dictType, value)).getLabel();
    }

    @SneakyThrows
    public static String parseDictDataValue(String dictType, String label) {
        return PARSE_DICT_DATA_CACHE.get(new KeyValue<>(dictType, label)).getValue();
    }

}