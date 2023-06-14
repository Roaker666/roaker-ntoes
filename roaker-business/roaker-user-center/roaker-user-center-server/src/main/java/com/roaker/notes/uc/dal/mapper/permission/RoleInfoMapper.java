package com.roaker.notes.uc.dal.mapper.permission;

import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.vo.permission.RolePageReqVO;
import com.roaker.notes.uc.vo.permission.RoleExportReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface RoleInfoMapper extends BaseMapperX<RoleInfoDO> {

    default PageResult<RoleInfoDO> selectPage(RolePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RoleInfoDO>()
                .likeIfPresent(RoleInfoDO::getRoleName, reqVO.getName())
                .likeIfPresent(RoleInfoDO::getRoleId, reqVO.getCode())
                .eqIfPresent(RoleInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BaseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoleInfoDO::getId));
    }

    default List<RoleInfoDO> selectList(RoleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RoleInfoDO>()
                .likeIfPresent(RoleInfoDO::getRoleName, reqVO.getName())
                .likeIfPresent(RoleInfoDO::getRoleId, reqVO.getCode())
                .eqIfPresent(RoleInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BaseDO::getCreateTime, reqVO.getCreateTime()));
    }

    default RoleInfoDO selectByName(String name) {
        return selectOne(RoleInfoDO::getRoleName, name);
    }

    default RoleInfoDO selectByCode(String code) {
        return selectOne(RoleInfoDO::getRoleId, code);
    }

    default List<RoleInfoDO> selectListByStatus(@Nullable Collection<Integer> statuses) {
        return selectList(RoleInfoDO::getStatus, statuses);
    }

}
