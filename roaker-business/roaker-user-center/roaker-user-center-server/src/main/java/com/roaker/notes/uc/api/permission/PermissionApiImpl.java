package com.roaker.notes.uc.api.permission;

import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.api.permission.dto.DeptDataPermissionRespDTO;
import com.roaker.notes.uc.service.permission.PermissionInfoService;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * 权限 API 实现类
 *
 * @author Roaker
 */
@Service
public class PermissionApiImpl implements PermissionApi {

    @Resource
    private PermissionInfoService permissionService;

    @Resource
    private RolePermissionCoreService rolePermissionCoreService;

    @Override
    public Set<String> getUserRoleIdListByRoleIds(Collection<Long> roleIds) {
        return rolePermissionCoreService.getUserRoleIdListByRoleId(roleIds);
    }

    @Override
    public boolean hasAnyPermissions(String userId, String... permissions) {
        return rolePermissionCoreService.hasAnyPermissions(userId, permissions);
    }

    @Override
    public boolean hasAnyRoles(String userId, String... role) {
        return rolePermissionCoreService.hasAnyRoles(userId, role);
    }

    @Override
    public DeptDataPermissionRespDTO getDeptDataPermission(String userId) {
        return rolePermissionCoreService.getDeptDataPermission(userId);
    }

}
