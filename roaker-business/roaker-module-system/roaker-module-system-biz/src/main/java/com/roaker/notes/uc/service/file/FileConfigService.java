package com.roaker.notes.uc.service.file;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.file.core.client.FileClient;
import com.roaker.notes.uc.dal.dataobject.file.FileConfigDO;
import com.roaker.notes.uc.vo.file.FileConfigCreateReqVO;
import com.roaker.notes.uc.vo.file.FileConfigPageReqVO;
import com.roaker.notes.uc.vo.file.FileConfigUpdateReqVO;
import jakarta.validation.Valid;

/**
 * 文件配置 Service 接口
 * @author lei.rao
 * @since 1.0
 */
public interface FileConfigService {
    /**
     * 初始化文件客户端
     */
    void initLocalCache();

    /**
     * 创建文件配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFileConfig(@Valid FileConfigCreateReqVO createReqVO);
    /**
     * 更新文件配置
     *
     * @param updateReqVO 更新信息
     */
    void updateFileConfig(@Valid FileConfigUpdateReqVO updateReqVO);
    /**
     * 更新文件配置为 Master
     *
     * @param id 编号
     */
    void updateFileConfigMaster(Long id);
    /**
     * 删除文件配置
     *
     * @param id 编号
     */
    void deleteFileConfig(Long id);
    /**
     * 获得文件配置
     *
     * @param id 编号
     * @return 文件配置
     */
    FileConfigDO getFileConfig(Long id);
    /**
     * 获得文件配置分页
     *
     * @param pageReqVO 分页查询
     * @return 文件配置分页
     */
    PageResult<FileConfigDO> getFileConfigPage(FileConfigPageReqVO pageReqVO);
    /**
     * 测试文件配置是否正确，通过上传文件
     *
     * @param id 编号
     * @return 文件 URL
     */
    String testFileConfig(Long id) throws Exception;
    /**
     * 获得指定编号的文件客户端
     *
     * @param id 配置编号
     * @return 文件客户端
     */
    FileClient getFileClient(Long id);
    /**
     * 获得 Master 文件客户端
     *
     * @return 文件客户端
     */
    FileClient getMasterFileClient();

}
