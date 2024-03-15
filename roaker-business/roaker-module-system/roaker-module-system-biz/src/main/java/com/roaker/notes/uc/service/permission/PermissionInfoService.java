package com.roaker.notes.uc.service.permission;


import com.roaker.notes.uc.api.permission.dto.DeptDataPermissionRespDTO;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.vo.permission.PermissionInfoCreateReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoListReqVO;
import com.roaker.notes.uc.vo.permission.PermissionInfoUpdateReqVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;

/**
 * 菜单 Service 接口
 *
 * @author lei.rao
 */
public interface PermissionInfoService {

    /**
     * 创建菜单
     *
     * @param createReqVO 菜单信息
     * @return 创建出来的菜单编号
     */
    Long createMenu(PermissionInfoCreateReqVO createReqVO);

    /**
     * 更新菜单
     *
     * @param updateReqVO 菜单信息
     */
    void updateMenu(PermissionInfoUpdateReqVO updateReqVO);

    /**
     * 删除菜单
     *
     * @param id 菜单编号
     */
    void deleteMenu(Long id);

    /**
     * 获得所有菜单列表
     *
     * @return 菜单列表
     */
    List<PermissionInfoDO> getMenuList();

    /**
     * 基于租户，筛选菜单列表
     * 注意，如果是系统租户，返回的还是全菜单
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单列表
     */
    List<PermissionInfoDO> getMenuListByTenant(PermissionInfoListReqVO reqVO);

    /**
     * 筛选菜单列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单列表
     */
    List<PermissionInfoDO> getMenuList(PermissionInfoListReqVO reqVO);

    /**
     * 获得权限对应的菜单编号数组
     *
     * @param permission 权限标识
     * @return 数组
     */
    List<Long> getMenuIdListByPermissionFromCache(String permission);

    /**
     * 获得菜单
     *
     * @param id 菜单编号
     * @return 菜单
     */
    PermissionInfoDO getMenu(Long id);

    /**
     * 获得菜单数组
     *
     * @param ids 菜单编号数组
     * @return 菜单数组
     */
    List<PermissionInfoDO> getMenuList(Collection<Long> ids);

}
