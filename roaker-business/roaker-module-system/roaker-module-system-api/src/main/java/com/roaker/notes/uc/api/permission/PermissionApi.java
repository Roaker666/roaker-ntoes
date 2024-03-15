package com.roaker.notes.uc.api.permission;

import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.api.permission.dto.DeptDataPermissionRespDTO;

import java.util.Collection;
import java.util.Set;

public interface PermissionApi {

    /**
     * 获得拥有多个角色的用户编号集合
     *
     * @param roleIds 角色编号集合
     * @return 用户编号集合
     */
    Set<String> getUserRoleIdListByRoleIds(Collection<Long> roleIds);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param userId 用户编号
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(String userId, String... permissions);

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param userId 用户编号
     * @param roles 角色数组
     * @return 是否
     */
    boolean hasAnyRoles(String userId, String... roles);

    /**
     * 获得登陆用户的部门数据权限
     *
     * @param userId 用户编号
     * @return 部门数据权限
     */
    DeptDataPermissionRespDTO getDeptDataPermission(String userId);


}