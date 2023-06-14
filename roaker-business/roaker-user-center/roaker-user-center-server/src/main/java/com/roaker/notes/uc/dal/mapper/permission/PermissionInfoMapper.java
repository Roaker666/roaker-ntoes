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

    default PermissionInfoDO selectByParentIdAndName(String parentId, String name) {
        return selectOne(PermissionInfoDO::getParentId, parentId, PermissionInfoDO::getPermissionName, name);
    }

    default PermissionInfoDO selectByPermissionId(String permissionId) {
        return selectOne(PermissionInfoDO::getPermissionId, permissionId);
    }

    default Long selectCountByParentId(String parentId) {
        return selectCount(PermissionInfoDO::getParentId, parentId);
    }

    default List<PermissionInfoDO> selectList(PermissionInfoListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PermissionInfoDO>()
                .likeIfPresent(PermissionInfoDO::getPermissionName, reqVO.getName())
                .eqIfPresent(PermissionInfoDO::getStatus, reqVO.getStatus()));
    }

}
