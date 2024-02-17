package com.roaker.notes.uc.service.user;

import cn.hutool.core.collection.CollUtil;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.vo.user.*;
import jakarta.validation.Valid;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AdminUserService {

    /**
     * 创建用户
     *
     * @param reqVO 用户信息
     * @return 用户编号
     */
    String createUser(@Valid UserCreateReqVO reqVO);

    /**
     * 修改用户
     *
     * @param reqVO 用户信息
     */
    void updateUser(@Valid UserUpdateReqVO reqVO);

    /**
     * 更新用户的最后登陆信息
     *
     * @param uid 用户编号
     * @param loginIp 登陆 IP
     */
    void updateUserLogin(String uid, String loginIp);

    /**
     * 修改用户个人信息
     *
     * @param uid 用户编号
     * @param reqVO 用户个人信息
     */
    void updateUserProfile(String uid, @Valid UserProfileUpdateReqVO reqVO);

    /**
     * 修改用户个人密码
     *
     * @param uid 用户编号
     * @param reqVO 更新用户个人密码
     */
    void updateUserPassword(String uid, @Valid UserProfileUpdatePasswordReqVO reqVO);

    /**
     * 更新用户头像
     *
     * @param uid         用户 uid
     * @param avatarFile 头像文件
     */
    String updateUserAvatar(String uid, InputStream avatarFile) throws Exception;

    /**
     * 修改密码
     *
     * @param uid       用户编号
     * @param password 密码
     */
    void updateUserPassword(String uid, String password);

    /**
     * 修改状态
     *
     * @param uid     用户编号
     * @param status 状态
     */
    void updateUserStatus(String uid, Integer status);

    /**
     * 删除用户
     *
     * @param uid 用户编号
     */
    void deleteUser(String id);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    AdminUserInfoDO getUserByUsername(String username);

    /**
     * 通过手机号获取用户
     *
     * @param mobile 手机号
     * @return 用户对象信息
     */
    AdminUserInfoDO getUserByMobile(String mobile);

    /**
     * 获得用户分页列表
     *
     * @param reqVO 分页条件
     * @return 分页列表
     */
    PageResult<AdminUserInfoDO> getUserPage(UserPageReqVO reqVO);

    /**
     * 通过用户 ID 查询用户
     *
     * @param uid 用户ID
     * @return 用户对象信息
     */
    AdminUserInfoDO getUser(String uid);

    /**
     * 获得指定部门的用户数组
     *
     * @param deptIds 部门数组
     * @return 用户数组
     */
    List<AdminUserInfoDO> getUserListByDeptIds(Collection<Long> deptIds);

    /**
     * 获得指定岗位的用户数组
     *
     * @param postIds 岗位数组
     * @return 用户数组
     */
    List<AdminUserInfoDO> getUserListByPostIds(Collection<Long> postIds);

    /**
     * 获得用户列表
     *
     * @param ids 用户编号数组
     * @return 用户列表
     */
    List<AdminUserInfoDO> getUserList(Collection<String> uidList);

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param uidList 用户编号数组
     */
    void validateUserList(Collection<String> uidList);

    /**
     * 获得用户 Map
     *
     * @param uidList 用户编号数组
     * @return 用户 Map
     */
    default Map<String, AdminUserInfoDO> getUserMap(Collection<String> uidList) {
        if (CollUtil.isEmpty(uidList)) {
            return new HashMap<>();
        }
        return RoakerCollectionUtils.convertMap(getUserList(uidList), AdminUserInfoDO::getUid);
    }

    /**
     * 获得用户列表
     *
     * @param reqVO 列表请求
     * @return 用户列表
     */
    List<AdminUserInfoDO> getUserList(UserExportReqVO reqVO);

    /**
     * 获得用户列表，基于昵称模糊匹配
     *
     * @param nickname 昵称
     * @return 用户列表
     */
    List<AdminUserInfoDO> getUserListByNickname(String nickname);

    /**
     * 批量导入用户
     *
     * @param importUsers     导入用户列表
     * @param isUpdateSupport 是否支持更新
     * @return 导入结果
     */
    UserImportRespVO importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport);

    /**
     * 获得指定状态的用户们
     *
     * @param status 状态
     * @return 用户们
     */
    List<AdminUserInfoDO> getUserListByStatus(Integer status);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword 未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

}