package com.roaker.notes.infra.encrypt.service;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileDO;
import com.roaker.notes.infra.encrypt.vo.FilePageReqVO;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface FileService {

    /**
     * 获得文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO);

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name 文件名称
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    String createFile(String name, String path, byte[] content);

    /**
     * 删除文件
     *
     * @param id 编号
     */
    void deleteFile(Long id) throws Exception;
    /**
     * 获得文件内容
     *
     * @param configId 配置编号
     * @param path 文件路径
     * @return 文件内容
     */
    byte[] getFileContent(Long configId, String path) throws Exception;

}
