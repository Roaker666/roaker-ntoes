package com.roaker.notes.uc.dal.mapper.permission;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.vo.permission.PermissionInfoListReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface PermissionInfoMapper extends BaseMapperX<PermissionInfoDO> {

    default PermissionInfoDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(PermissionInfoDO::getParentId, parentId, PermissionInfoDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(PermissionInfoDO::getParentId, parentId);
    }

    default List<PermissionInfoDO> selectList(PermissionInfoListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PermissionInfoDO>()
                .likeIfPresent(PermissionInfoDO::getName, reqVO.getName())
                .eqIfPresent(PermissionInfoDO::getStatus, reqVO.getStatus()));
    }

    default List<PermissionInfoDO> selectListByPermission(String permission) {
        return selectList(PermissionInfoDO::getPermission, permission);
    }

}
