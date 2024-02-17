package com.roaker.notes.uc.converter.db;

import com.roaker.notes.uc.dal.dataobject.db.DataSourceConfigDO;
import com.roaker.notes.uc.vo.db.DataSourceConfigCreateReqVO;
import com.roaker.notes.uc.vo.db.DataSourceConfigRespVO;
import com.roaker.notes.uc.vo.db.DataSourceConfigUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据源配置 Convert
 *
 * @author Roaker
 */
@Mapper
public interface DataSourceConfigConvert {

    DataSourceConfigConvert INSTANCE = Mappers.getMapper(DataSourceConfigConvert.class);

    DataSourceConfigDO convert(DataSourceConfigCreateReqVO bean);

    DataSourceConfigDO convert(DataSourceConfigUpdateReqVO bean);

    DataSourceConfigRespVO convert(DataSourceConfigDO bean);

    List<DataSourceConfigRespVO> convertList(List<DataSourceConfigDO> list);

}
