package com.roaker.notes.file.core.client;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.roaker.notes.file.core.enums.FileStorageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 文件客户端的工厂实现类
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class FileClientFactoryImpl implements FileClientFactory {
    /**
     * 文件客户端 Map
     * key：配置编号
     */
    private final ConcurrentMap<Long, AbstractFileClient<?>> clients = new ConcurrentHashMap<>();

    @Override
    public FileClient getFileClient(Long configId) {
        AbstractFileClient<?> client = clients.get(configId);
        if (client == null) {
            log.error("[getFileClient][配置编号({}) 找不到客户端]", configId);
        }
        return client;
    }

    @Override
    public <Config extends FileClientConfig> void createOrUpdateFileClient(Long configId, FileStorageEnum storageEnum, Config config) {
        @SuppressWarnings("unchecked")
        AbstractFileClient<Config> client = (AbstractFileClient<Config>) clients.get(configId);
        if (client == null) {
            client = this.createFileClient(configId, storageEnum, config);
            client.init();
            clients.put(client.getId(), client);
        } else {
            client.refresh(config);
        }
    }
    @SuppressWarnings("unchecked")
    private <Config extends FileClientConfig> AbstractFileClient<Config> createFileClient(
            Long configId, FileStorageEnum storageEnum, Config config) {
        // 创建客户端
        return (AbstractFileClient<Config>) ReflectUtil.newInstance(storageEnum.getClientClass(), configId, config);
    }
}
