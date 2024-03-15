package com.roaker.notes.uc.dal.mapper.file;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.roaker.notes.file.core.client.db.DBFileContentFrameworkDAO;
import com.roaker.notes.uc.dal.dataobject.file.FileContentDO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author lei.rao
 * @since 1.0
 */
@Repository
public class FileContentDAOImpl implements DBFileContentFrameworkDAO {
    @Resource
    private FileContentMapper fileContentMapper;

    @Override
    public void insert(Long configId, String path, byte[] content) {
        FileContentDO entity = FileContentDO.builder()
                .configId(configId)
                .path(path)
                .content(content)
                .build();
        fileContentMapper.insert(entity);
    }

    @Override
    public void delete(Long configId, String path) {
        fileContentMapper.delete(buildQuery(configId, path));
    }

    @Override
    public byte[] selectContent(Long configId, String path) {
        List<FileContentDO> list = fileContentMapper.selectList(buildQuery(configId, path).select(FileContentDO::getContent).orderByDesc(FileContentDO::getId));
        return Optional.ofNullable(CollUtil.getFirst(list))
                .map(FileContentDO::getContent)
                .orElse(null);
    }

    private LambdaQueryWrapper<FileContentDO> buildQuery(Long configId, String path) {
        return new LambdaQueryWrapper<FileContentDO>()
                .eq(FileContentDO::getConfigId, configId)
                .eq(FileContentDO::getPath, path);
    }
}
