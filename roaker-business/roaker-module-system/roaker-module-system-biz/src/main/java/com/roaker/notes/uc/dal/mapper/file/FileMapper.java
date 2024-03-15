package com.roaker.notes.uc.dal.mapper.file;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.dal.dataobject.file.FileDO;
import com.roaker.notes.uc.vo.file.FilePageReqVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface FileMapper extends BaseMapperX<FileDO> {
    default PageResult<FileDO> selectPage(FilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .betweenIfPresent(FileDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FileDO::getId));
    }
}
