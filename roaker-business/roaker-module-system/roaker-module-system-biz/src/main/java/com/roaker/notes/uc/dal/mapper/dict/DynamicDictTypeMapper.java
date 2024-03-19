package com.roaker.notes.uc.dal.mapper.dict;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.dal.dataobject.dict.DynamicDictTypeDO;
import com.roaker.notes.uc.vo.dict.type.DictTypePageReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface DynamicDictTypeMapper extends BaseMapperX<DynamicDictTypeDO> {
    default PageResult<DynamicDictTypeDO> selectPage(DictTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DynamicDictTypeDO>()
                .likeIfPresent(DynamicDictTypeDO::getName, reqVO.getName())
                .likeIfPresent(DynamicDictTypeDO::getDictType, reqVO.getType())
                .eqIfPresent(DynamicDictTypeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DynamicDictTypeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DynamicDictTypeDO::getId));
    }

    default DynamicDictTypeDO selectByType(String type) {
        return selectOne(DynamicDictTypeDO::getDictType, type);
    }

    default DynamicDictTypeDO selectByName(String name) {
        return selectOne(DynamicDictTypeDO::getName, name);
    }

    @Update("UPDATE system_dict_type SET deleted = 1, deleted_time = #{deletedTime} WHERE id = #{id}")
    void updateToDelete(@Param("id") Long id, @Param("deletedTime") LocalDateTime deletedTime);
}
