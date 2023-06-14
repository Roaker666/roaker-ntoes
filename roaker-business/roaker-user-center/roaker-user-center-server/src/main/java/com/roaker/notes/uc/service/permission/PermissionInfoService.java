package com.roaker.notes.uc.service.permission;


import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.vo.permission.PermissionInfoCreateReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoListReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoUpdateReqVO;

import java.util.Collection;
import java.util.List;

/**
 * 菜单 Service 接口
 *
 * @author 芋道源码
 */
public interface PermissionInfoService {

    /**
     * 初始化菜单的本地缓存
     */
    void initLocalCache();

    /**
     * 创建菜单
     *
     * @param reqVO 菜单信息
     * @return 创建出来的菜单编号
     */
    Long createPermissionInfo(PermissionInfoCreateReqVO reqVO);

    /**
     * 更新菜单
     *
     * @param reqVO 菜单信息
     */
    void updatePermissionInfo(PermissionInfoUpdateReqVO reqVO);

    /**
     * 删除菜单
     *
     * @param permissionId 菜单编号
     */
    void deletePermissionInfo(String permissionId);

    /**
     * 获得所有菜单列表
     *
     * @return 菜单列表
     */
    List<PermissionInfoDO> getPermissionInfoList();

    /**
     * 基于租户，筛选菜单列表
     * 注意，如果是系统租户，返回的还是全菜单
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单列表
     */
    List<PermissionInfoDO> getPermissionInfoListByTenant(PermissionInfoListReqVO reqVO);

    /**
     * 筛选菜单列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单列表
     */
    List<PermissionInfoDO> getPermissionInfoList(PermissionInfoListReqVO reqVO);

    /**
     * 获得所有菜单，从缓存中
     *
     * 任一参数为空时，则返回为空
     *
     * @param permissionInfoTypes 菜单类型数组
     * @param permissionInfosStatuses 菜单状态数组
     * @return 菜单列表
     */
    List<PermissionInfoDO> getPermissionInfoListFromCache(Collection<Integer> permissionInfoTypes, Collection<Integer> permissionInfosStatuses);

    /**
     * 获得指定编号的菜单数组，从缓存中
     *
     * 任一参数为空时，则返回为空
     *
     * @param permissionInfoIds 菜单编号数组
     * @param permissionInfoTypes 菜单类型数组
     * @param permissionInfosStatuses 菜单状态数组
     * @return 菜单数组
     */
    List<PermissionInfoDO> getPermissionInfoListFromCache(Collection<String> permissionInfoIds, Collection<Integer> permissionInfoTypes,
                                      Collection<Integer> permissionInfosStatuses);

    /**
     * 获得权限对应的菜单数组
     *
     * @param permissionInfo 权限标识
     * @return 数组
     */
    List<PermissionInfoDO> getPermissionInfoListByPermissionInfoFromCache(String permissionInfo);

    /**
     * 获得菜单
     *
     * @param permissionId 菜单编号
     * @return 菜单
     */
    PermissionInfoDO getPermissionInfo(String permissionId);

}
