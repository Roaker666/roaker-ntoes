package com.roaker.notes.infra.encrypt.dal.mapper;

import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileConfigDO;
import com.roaker.notes.infra.encrypt.vo.FileConfigPageReqVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface FileConfigMapper extends BaseMapperX<FileConfigDO> {
    default PageResult<FileConfigDO> selectPage(FileConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FileConfigDO>()
                .likeIfPresent(FileConfigDO::getName, reqVO.getName())
                .eqIfPresent(FileConfigDO::getStorage, reqVO.getStorage())
                .betweenIfPresent(FileConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FileConfigDO::getId));
    }
}
