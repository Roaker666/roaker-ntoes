package com.roaker.notes.file.core.client.db;

import cn.hutool.extra.spring.SpringUtil;
import com.roaker.notes.file.core.client.AbstractFileClient;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
public class DBFileClient extends AbstractFileClient<DBFileClientConfig> implements Serializable {
    private DBFileContentFrameworkDAO dao;

    public DBFileClient(Long id, DBFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {

    }

    @Override
    public String upload(byte[] content, String path, String type) throws Exception {
        getDao().insert(getId(), path, content);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public void delete(String path) throws Exception {
        getDao().delete(getId(), path);
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        return getDao().selectContent(getId(), path);
    }

    @Override
    public String getPath(String fileUrl) {
        return super.getPath(config.getDomain(), fileUrl);
    }

    private DBFileContentFrameworkDAO getDao() {
        // 延迟获取，因为 SpringUtil 初始化太慢
        if (dao == null) {
            dao = SpringUtil.getBean(DBFileContentFrameworkDAO.class);
        }
        return dao;
    }
}
