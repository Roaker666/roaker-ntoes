package com.roaker.notes.infra.encrypt.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.roaker.notes.commons.constants.ErrorCodeConstants;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.io.FileUtils;
import com.roaker.notes.file.core.client.FileClient;
import com.roaker.notes.file.core.utils.FileTypeUtils;
import com.roaker.notes.infra.encrypt.service.FileService;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileDO;
import com.roaker.notes.infra.encrypt.dal.mapper.FileMapper;
import com.roaker.notes.infra.encrypt.service.FileConfigService;
import com.roaker.notes.infra.encrypt.vo.FilePageReqVO;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;
    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        return url;
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        // 校验存在
        FileDO file = validateFileExists(id);

        // 从文件存储器中删除
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        // 删除记录
        fileMapper.deleteById(id);
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(ErrorCodeConstants.FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }
}
