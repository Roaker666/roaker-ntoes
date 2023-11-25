package com.roaker.notes.uc.dal.mapper.permission;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.dal.dataobject.permission.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface UserRoleMapper extends BaseMapperX<UserRoleDO> {

    default List<UserRoleDO> selectListByUserId(String userId, UserTypeEnum userType) {
        return selectList(new LambdaQueryWrapperX<UserRoleDO>()
                .eq(UserRoleDO::getUid, userId)
                .eq(UserRoleDO::getUserType, userType));
    }

    default void deleteListByUserIdAndRoleIdIds(String userId, UserTypeEnum userType, Collection<String> roleIds) {
        delete(new LambdaQueryWrapperX<UserRoleDO>()
                .eq(UserRoleDO::getUid, userId)
                .eq(UserRoleDO::getUserType, userType)
                .in(UserRoleDO::getRoleId, roleIds));
    }

    default void deleteListByUserId(String userId, UserTypeEnum userType) {
        delete(new LambdaQueryWrapperX<UserRoleDO>()
                .eq(UserRoleDO::getUid, userId)
                .eq(UserRoleDO::getUserType, userType));

    }

    default void deleteListByRoleId(String roleId) {
        delete(new LambdaQueryWrapperX<UserRoleDO>().eq(UserRoleDO::getRoleId, roleId));
    }

    default List<UserRoleDO> selectListByRoleIds(Collection<String> roleIds, UserTypeEnum userType) {
        return selectList(new LambdaQueryWrapperX<UserRoleDO>()
                .in(UserRoleDO::getRoleId, roleIds)
                .eq(UserRoleDO::getUserType, userType));
    }

}
