package com.roaker.notes.uc.service.permission;

import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 权限 Service 接口
 * <p>
 * 提供用户-角色、角色-菜单、角色-部门的关联权限处理
 *
 * @author 芋道源码
 */
public interface RolePermissionCoreService {

    /**
     * 初始化权限的本地缓存
     */
    void initLocalCache();

    /**
     * 获得角色们拥有的菜单列表，从缓存中获取
     * <p>
     * 任一参数为空时，则返回为空
     *
     * @param roleIds                 角色编号数组
     * @param PermissionInfoTypes     菜单类型数组
     * @param PermissionInfosStatuses 菜单状态数组
     * @return 菜单列表
     */
    List<PermissionInfoDO> getRolePermissionInfoListFromCache(Collection<String> roleIds, Collection<Integer> PermissionInfoTypes,
                                                              Collection<Integer> PermissionInfosStatuses);

    /**
     * 获得用户拥有的角色编号集合，从缓存中获取
     *
     * @param userId       用户编号
     * @param roleStatuses 角色状态集合. 允许为空，为空时不过滤
     * @return 角色编号集合
     */
    Set<String> getUserRoleIdsFromCache(String userId, @Nullable Collection<Integer> roleStatuses);

    /**
     * 获得角色拥有的菜单编号集合
     *
     * @param roleId 角色编号
     * @return 菜单编号集合
     */
    Set<String> getRolePermissionInfoIds(String roleId);

    /**
     * 获得拥有多个角色的用户编号集合
     *
     * @param roleIds 角色编号集合
     * @return 用户编号集合
     */
    Set<String> getUserRoleIdListByRoleIds(Collection<String> roleIds, UserTypeEnum userType);

    /**
     * 设置角色菜单
     *
     * @param roleId            角色编号
     * @param permissionInfoIds 菜单编号集合
     */
    void assignRolePermissionInfo(String roleId, Set<String> permissionInfoIds);

    /**
     * 获得用户拥有的角色编号集合
     *
     * @param userId 用户编号
     * @return 角色编号集合
     */
    Set<String> getUserRoleIdListByUserId(String userId,  UserTypeEnum userType);

    /**
     * 设置用户角色
     *
     * @param userId  角色编号
     * @param roleIds 角色编号集合
     */
    void assignUserRole(String userId, UserTypeEnum userType, Set<String> roleIds);

    /**
     * 处理角色删除时，删除关联授权数据
     *
     * @param roleId 角色编号
     */
    void processRoleDeleted(String roleId);

    /**
     * 处理菜单删除时，删除关联授权数据
     *
     * @param permissionInfoId 菜单编号
     */
    void processPermissionInfoDeleted(String permissionInfoId);

    /**
     * 处理用户删除是，删除关联授权数据
     *
     * @param userId 用户编号
     */
    void processUserDeleted(String userId);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param userId      用户编号
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(String userId, String... permissions);

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param roles 角色数组
     * @return 是否
     */
    boolean hasAnyRoles(String userId, String... roles);

}
