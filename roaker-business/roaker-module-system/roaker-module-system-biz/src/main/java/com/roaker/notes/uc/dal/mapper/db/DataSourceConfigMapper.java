package com.roaker.notes.uc.dal.mapper.db;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.uc.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源配置 Mapper
 *
 * @author Roaker
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
