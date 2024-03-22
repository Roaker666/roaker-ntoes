package com.roaker.notes.uc.service.file.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import com.gitee.sunchenbin.mybatis.actable.manager.handler.StartUpHandler;
import com.roaker.notes.commons.constants.ErrorCodeConstants;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.commons.utils.ValidationUtils;
import com.roaker.notes.file.core.client.FileClient;
import com.roaker.notes.file.core.client.FileClientConfig;
import com.roaker.notes.file.core.client.FileClientFactory;
import com.roaker.notes.file.core.enums.FileStorageEnum;
import com.roaker.notes.uc.converter.file.FileConfigConvert;
import com.roaker.notes.uc.dal.dataobject.file.FileConfigDO;
import com.roaker.notes.uc.dal.mapper.file.FileConfigMapper;
import com.roaker.notes.uc.service.file.FileConfigService;
import com.roaker.notes.uc.vo.file.FileConfigCreateReqVO;
import com.roaker.notes.uc.vo.file.FileConfigPageReqVO;
import com.roaker.notes.uc.vo.file.FileConfigUpdateReqVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.FILE_CONFIG_DELETE_FAIL_MASTER;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@Validated
@Slf4j
@Order(Integer.MAX_VALUE)
public class FileConfigServiceImpl implements FileConfigService {
    @Resource
    private FileClientFactory fileClientFactory;

    /**
     * Master FileClient 对象，有且仅有一个，即 {@link FileConfigDO#getMaster()} 对应的
     */
    @Getter
    private FileClient masterFileClient;

    @Resource
    private FileConfigMapper fileConfigMapper;
    //    @Resource
//    private FileConfigProducer fileConfigProducer;
    @Resource
    private Validator validator;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    @PostConstruct
    public void initLocalCache() {
//        // 先加载
//        applicationContext.getBean(StartUpHandler.class);
//        // 第一步：查询数据
//        List<FileConfigDO> configs = fileConfigMapper.selectList();
//        log.info("[initLocalCache][缓存文件配置，数量为:{}]", configs.size());
//        // 第二步：构建缓存：创建或更新文件 Client
//        configs.forEach(config -> {
//            fileClientFactory.createOrUpdateFileClient(config.getId(), config.getStorage(), config.getConfig());
//            // 如果是 master，进行设置
//            if (Boolean.TRUE.equals(config.getMaster())) {
//                masterFileClient = fileClientFactory.getFileClient(config.getId());
//            }
//        });
    }

    @Override
    public Long createFileConfig(FileConfigCreateReqVO createReqVO) {
        // 插入
        FileConfigDO fileConfig = FileConfigConvert.INSTANCE.convert(createReqVO)
                .setConfig(parseClientConfig(createReqVO.getStorage(), createReqVO.getConfig()))
                .setMaster(false); // 默认非 master
        fileConfigMapper.insert(fileConfig);
        // 发送刷新配置的消息
//        fileConfigProducer.sendFileConfigRefreshMessage();
        // 返回
        return fileConfig.getId();
    }
    private FileClientConfig parseClientConfig(Integer storage, Map<String, Object> config) {
        // 获取配置类
        Class<? extends FileClientConfig> configClass = FileStorageEnum.getByStorage(storage)
                .getConfigClass();
        FileClientConfig clientConfig = JacksonUtils.from(JacksonUtils.toJSON(config), configClass);
        // 参数校验
        ValidationUtils.validate(validator, clientConfig);
        // 设置参数
        return clientConfig;
    }
    @Override
    public void updateFileConfig(FileConfigUpdateReqVO updateReqVO) {
        // 校验存在
        FileConfigDO config = validateFileConfigExists(updateReqVO.getId());
        // 更新
        FileConfigDO updateObj = FileConfigConvert.INSTANCE.convert(updateReqVO)
                .setConfig(parseClientConfig(config.getStorage().getStorage(), updateReqVO.getConfig()));
        fileConfigMapper.updateById(updateObj);
        // 发送刷新配置的消息
//        fileConfigProducer.sendFileConfigRefreshMessage();
    }

    private FileConfigDO validateFileConfigExists(Long id) {
        FileConfigDO config = fileConfigMapper.selectById(id);
        if (config == null) {
            throw exception(ErrorCodeConstants.FILE_CONFIG_NOT_EXISTS);
        }
        return config;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFileConfigMaster(Long id) {
        // 校验存在
        validateFileConfigExists(id);
        // 更新其它为非 master
        fileConfigMapper.updateBatch(new FileConfigDO().setMaster(false));
        // 更新
        fileConfigMapper.updateById(new FileConfigDO().setId(id).setMaster(true));
        // 发送刷新配置的消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
//                fileConfigProducer.sendFileConfigRefreshMessage();
            }

        });
    }

    @Override
    public void deleteFileConfig(Long id) {
        // 校验存在
        FileConfigDO config = validateFileConfigExists(id);
        if (Boolean.TRUE.equals(config.getMaster())) {
            throw exception(FILE_CONFIG_DELETE_FAIL_MASTER);
        }
        // 删除
        fileConfigMapper.deleteById(id);
        // 发送刷新配置的消息
    }

    @Override
    public FileConfigDO getFileConfig(Long id) {
        return fileConfigMapper.selectById(id);
    }

    @Override
    public PageResult<FileConfigDO> getFileConfigPage(FileConfigPageReqVO pageReqVO) {
        return fileConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public String testFileConfig(Long id) throws Exception {
        // 校验存在bejson.jpg
        validateFileConfigExists(id);
        // 上传文件
        byte[] content = ResourceUtil.readBytes("file/test-file.jpg");
        return fileClientFactory.getFileClient(id).upload(content, IdUtil.fastSimpleUUID() + ".jpg", "image/jpeg");
    }

    @Override
    public FileClient getFileClient(Long id) {
        return fileClientFactory.getFileClient(id);
    }
}
