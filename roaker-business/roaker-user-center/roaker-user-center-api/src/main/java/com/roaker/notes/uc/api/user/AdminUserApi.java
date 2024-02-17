package com.roaker.notes.uc.api.user;

import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.uc.vo.user.AdminUserRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Admin 用户 API 接口
 *
 * @author Roaker
 */
public interface AdminUserApi {

    /**
     * 通过用户 ID 查询用户
     *
     * @param uid 用户ID
     * @return 用户对象信息
     */
    AdminUserRespDTO getUser(String uid);

    /**
     * 通过用户 ID 查询用户们
     *
     * @param uidList 用户 ID 们
     * @return 用户对象信息
     */
    List<AdminUserRespDTO> getUserList(Collection<String> uidList);

    /**
     * 获得指定部门的用户数组
     *
     * @param deptIds 部门数组
     * @return 用户数组
     */
    List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds);

    /**
     * 获得指定岗位的用户数组
     *
     * @param postIds 岗位数组
     * @return 用户数组
     */
    List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds);

    /**
     * 获得用户 Map
     *
     * @param uidList 用户编号数组
     * @return 用户 Map
     */
    default Map<String, AdminUserRespDTO> getUserMap(Collection<String> uidList) {
        List<AdminUserRespDTO> users = getUserList(uidList);
        return RoakerCollectionUtils.convertMap(users, AdminUserRespDTO::getUid);
    }

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param uidList 用户编号数组
     */
    void validateUserList(Collection<String> uidList);

}
