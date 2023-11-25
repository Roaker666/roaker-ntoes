package com.roaker.notes.uc.dal.mapper.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.uc.dal.dataobject.permission.RolePermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface RolePermissionMapper extends BaseMapperX<RolePermissionDO> {

    @Repository
    class BatchInsertMapper extends ServiceImpl<RolePermissionMapper, RolePermissionDO> {
    }

    default List<RolePermissionDO> selectListByRoleId(String roleId) {
        return selectList(RolePermissionDO::getRoleId, roleId);
    }

    default void deleteListByRoleIdAndPermissionIds(String roleId, Collection<String> permissionIds) {
        delete(new LambdaQueryWrapper<RolePermissionDO>()
                .eq(RolePermissionDO::getRoleId, roleId)
                .in(RolePermissionDO::getPermissionId, permissionIds));
    }

    default void deleteListByPermissionId(String permissionId) {
        delete(new LambdaQueryWrapper<RolePermissionDO>().eq(RolePermissionDO::getPermissionId, permissionId));
    }

    default void deleteListByRoleId(String roleId) {
        delete(new LambdaQueryWrapper<RolePermissionDO>().eq(RolePermissionDO::getRoleId, roleId));
    }

}