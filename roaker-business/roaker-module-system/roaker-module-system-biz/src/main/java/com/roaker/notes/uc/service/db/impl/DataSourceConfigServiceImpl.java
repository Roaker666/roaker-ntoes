package com.roaker.notes.uc.service.db.impl;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.roaker.notes.commons.db.util.JdbcUtils;
import com.roaker.notes.uc.converter.db.DataSourceConfigConvert;
import com.roaker.notes.uc.dal.dataobject.db.DataSourceConfigDO;
import com.roaker.notes.uc.dal.mapper.db.DataSourceConfigMapper;
import com.roaker.notes.uc.service.db.DataSourceConfigService;
import com.roaker.notes.uc.vo.db.DataSourceConfigCreateReqVO;
import com.roaker.notes.uc.vo.db.DataSourceConfigUpdateReqVO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.DATA_SOURCE_CONFIG_NOT_EXISTS;
import static com.roaker.notes.commons.constants.ErrorCodeConstants.DATA_SOURCE_CONFIG_NOT_OK;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;


/**
 * 数据源配置 Service 实现类
 *
 * @author Roaker
 */
@Service
@Validated
public class DataSourceConfigServiceImpl implements DataSourceConfigService {

    @Resource
    private DataSourceConfigMapper dataSourceConfigMapper;

    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Override
    public Long createDataSourceConfig(DataSourceConfigCreateReqVO createReqVO) {
        DataSourceConfigDO dataSourceConfig = DataSourceConfigConvert.INSTANCE.convert(createReqVO);
        validateConnectionOK(dataSourceConfig);

        // 插入
        dataSourceConfigMapper.insert(dataSourceConfig);
        // 返回
        return dataSourceConfig.getId();
    }

    @Override
    public void updateDataSourceConfig(DataSourceConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateDataSourceConfigExists(updateReqVO.getId());
        DataSourceConfigDO updateObj = DataSourceConfigConvert.INSTANCE.convert(updateReqVO);
        validateConnectionOK(updateObj);

        // 更新
        dataSourceConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteDataSourceConfig(Long id) {
        // 校验存在
        validateDataSourceConfigExists(id);
        // 删除
        dataSourceConfigMapper.deleteById(id);
    }

    private void validateDataSourceConfigExists(Long id) {
        if (dataSourceConfigMapper.selectById(id) == null) {
            throw exception(DATA_SOURCE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public DataSourceConfigDO getDataSourceConfig(Long id) {
        // 如果 id 为 0，默认为 master 的数据源
        if (Objects.equals(id, DataSourceConfigDO.ID_MASTER)) {
            return buildMasterDataSourceConfig();
        }
        // 从 DB 中读取
        return dataSourceConfigMapper.selectById(id);
    }

    @Override
    public List<DataSourceConfigDO> getDataSourceConfigList() {
        List<DataSourceConfigDO> result = dataSourceConfigMapper.selectList();
        // 补充 master 数据源
        result.add(0, buildMasterDataSourceConfig());
        return result;
    }

    private void validateConnectionOK(DataSourceConfigDO config) {
        boolean success = JdbcUtils.isConnectionOK(config.getUrl(), config.getUsername(), config.getPassword());
        if (!success) {
            throw exception(DATA_SOURCE_CONFIG_NOT_OK);
        }
    }

    private DataSourceConfigDO buildMasterDataSourceConfig() {
        String primary = dynamicDataSourceProperties.getPrimary();
        DataSourceProperty dataSourceProperty = dynamicDataSourceProperties.getDatasource().get(primary);
        return new DataSourceConfigDO().setId(DataSourceConfigDO.ID_MASTER).setName(primary)
                .setUrl(dataSourceProperty.getUrl())
                .setUsername(dataSourceProperty.getUsername())
                .setPassword(dataSourceProperty.getPassword());
    }

}
