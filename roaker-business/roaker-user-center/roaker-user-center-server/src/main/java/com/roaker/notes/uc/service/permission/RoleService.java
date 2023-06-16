package com.roaker.notes.uc.service.permission;

import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.vo.permission.RolePageReqVO;
import com.roaker.notes.uc.vo.permission.RoleCreateReqVO;
import com.roaker.notes.uc.vo.permission.RoleExportReqVO;
import com.roaker.notes.uc.vo.permission.RoleUpdateReqVO;
import jakarta.validation.Valid;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色 Service 接口
 *
 * @author lei.rao
 */
public interface RoleService {

    /**
     * 初始化角色的本地缓存
     */
    void initLocalCache();

    /**
     * 创建角色
     *
     * @param reqVO 创建角色信息
     * @param type 角色类型
     * @return 角色编号
     */
    String createRole(@Valid RoleCreateReqVO reqVO, Integer type);

    /**
     * 更新角色
     *
     * @param reqVO 更新角色信息
     */
    void updateRole(@Valid RoleUpdateReqVO reqVO);

    /**
     * 删除角色
     *
     * @param roleId 角色编号
     */
    void deleteRole(String roleId);

    /**
     * 更新角色状态
     *
     * @param roleId 角色编号
     * @param status 状态
     */
    void updateRoleStatus(String roleId, Integer status);

    /**
     * 获得角色，从缓存中
     *
     * @param roleId 角色编号
     * @return 角色
     */
    RoleInfoDO getRoleFromCache(String roleId);

    /**
     * 获得角色列表
     *
     * @param statuses 筛选的状态。允许空，空时不筛选
     * @return 角色列表
     */
    List<RoleInfoDO> getRoleListByStatus(@Nullable Collection<Integer> statuses);

    /**
     * 获得角色数组，从缓存中
     *
     * @param roleIds 角色编号数组
     * @return 角色数组
     */
    List<RoleInfoDO> getRoleListFromCache(Collection<String> roleIds);

    /**
     * 判断角色数组中，是否有超级管理员
     *
     * @param roleList 角色数组
     * @return 是否有管理员
     */
    boolean hasAnySuperAdmin(Collection<RoleInfoDO> roleList);

    /**
     * 判断角色编号数组中，是否有管理员
     *
     * @param roleIds 角色编号数组
     * @return 是否有管理员
     */
    default boolean hasAnySuperAdmin(Set<String> roleIds) {
        return hasAnySuperAdmin(getRoleListFromCache(roleIds));
    }

    /**
     * 获得角色
     *
     * @param roleId 角色编号
     * @return 角色
     */
    RoleInfoDO getRole(String roleId);

    /**
     * 获得角色分页
     *
     * @param reqVO 角色分页查询
     * @return 角色分页结果
     */
    PageResult<RoleInfoDO> getRolePage(RolePageReqVO reqVO);

    /**
     * 获得角色列表
     *
     * @param reqVO 列表查询
     * @return 角色列表
     */
    List<RoleInfoDO> getRoleList(RoleExportReqVO reqVO);

    /**
     * 校验角色们是否有效。如下情况，视为无效：
     * 1. 角色编号不存在
     * 2. 角色被禁用
     *
     * @param roleIds 角色编号数组
     */
    void validateRoleList(Collection<String> roleIds);

}
