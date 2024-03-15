package com.roaker.notes.file.core.client;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public abstract class AbstractFileClient<Config extends FileClientConfig> implements FileClient {
    private final long id;
    protected Config config;

    public AbstractFileClient(Long id, Config config) {
        this.id = id;
        this.config = config;
    }

    /**
     * 初始化
     */
    public final void init() {
        doInit();
        log.info("[init][配置({}) 初始化完成]", config);
    }

    /**
     * 自定义初始化
     */
    protected abstract void doInit();

    public final void refresh(Config config) {
        // 判断是否更新
        if (config.equals(this.config)) {
            return;
        }
        log.info("[refresh][配置({})发生变化，重新初始化]", config);
        this.config = config;
        // 初始化
        this.init();
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * 格式化文件的 URL 访问地址
     * 使用场景：local、ftp、db，通过 FileController 的 getFile 来获取文件内容
     *
     * @param domain 自定义域名
     * @param path   文件路径
     * @return URL 访问地址
     */
    protected String formatFileUrl(String domain, String path) {
        return StrUtil.format("{}/admin-api/infra/file/{}/get/{}", domain, getId(), path);
    }


    protected String getPath(String domain, String fileUrl) {
        return StringUtils.substring(fileUrl, StrUtil.format("{}/admin-api/infra/file/{}/get/", domain, getId()).length());
    }
}
