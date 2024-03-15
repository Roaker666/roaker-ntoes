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

    default List<RolePermissionDO> selectListByRoleId(Long roleId) {
        return selectList(RolePermissionDO::getRoleId, roleId);
    }

    default List<RolePermissionDO> selectListByRoleId(Collection<Long> roleIds) {
        return selectList(RolePermissionDO::getRoleId, roleIds);
    }

    default List<RolePermissionDO> selectListByMenuId(Long menuId) {
        return selectList(RolePermissionDO::getMenuId, menuId);
    }

    default void deleteListByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds) {
        delete(new LambdaQueryWrapper<RolePermissionDO>()
                .eq(RolePermissionDO::getRoleId, roleId)
                .in(RolePermissionDO::getMenuId, menuIds));
    }

    default void deleteListByMenuId(Long menuId) {
        delete(new LambdaQueryWrapper<RolePermissionDO>().eq(RolePermissionDO::getMenuId, menuId));
    }

    default void deleteListByRoleId(Long roleId) {
        delete(new LambdaQueryWrapper<RolePermissionDO>().eq(RolePermissionDO::getRoleId, roleId));
    }

}