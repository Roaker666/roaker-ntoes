package com.roaker.notes.uc.service.user.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.query.LambdaUpdateWrapperX;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.datapermission.core.util.DataPermissionUtils;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.exception.ServiceException;
import com.roaker.notes.uc.api.encrypt.FileApi;
import com.roaker.notes.uc.api.social.SocialClientApi;
import com.roaker.notes.uc.api.social.dto.SocialWxPhoneNumberInfoRespDTO;
import com.roaker.notes.uc.converter.user.UserCenterConvert;
import com.roaker.notes.uc.dal.dataobject.credentials.ShareUserCredentialsDO;
import com.roaker.notes.uc.dal.dataobject.dept.DeptDO;
import com.roaker.notes.uc.dal.dataobject.dept.UserPostDO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.dal.mapper.credentials.ShareUserCredentialsMapper;
import com.roaker.notes.uc.dal.mapper.dept.UserPostMapper;
import com.roaker.notes.uc.dal.mapper.user.AdminUserInfoMapper;
import com.roaker.notes.uc.enums.oauth2.CredentialsIdentifyTypeEnums;
import com.roaker.notes.uc.service.dept.DeptService;
import com.roaker.notes.uc.service.dept.PostService;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.service.user.AdminUserService;
import com.roaker.notes.uc.vo.user.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.*;
import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertList;
import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    @Value("${sys.user.init-password:123456}")
    private String userInitPassword;

    @Resource
    private AdminUserInfoMapper adminUserInfoMapper;

    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private RolePermissionCoreService rolePermissionCoreService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserPostMapper userPostMapper;

    @Resource
    private ShareUserCredentialsMapper shareUserCredentialsMapper;

    @Resource
    private FileApi fileApi;

    @Resource
    private SocialClientApi socialClientApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createUser(UserCreateReqVO reqVO) {
        // 校验正确性
        validateUserForCreateOrUpdate(null, reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        // 插入用户
        AdminUserInfoDO user = UserCenterConvert.INSTANCE.convert(reqVO);
        user.setStatus(CommonStatusEnum.ENABLE);
        adminUserInfoMapper.insert(user);
        ShareUserCredentialsDO userCredentials = new ShareUserCredentialsDO();
        userCredentials.setStatus(CommonStatusEnum.ENABLE);
        userCredentials.setIdentifier(user.getUid());
        userCredentials.setIdentityType(CredentialsIdentifyTypeEnums.PASSWORD);
        userCredentials.setUid(user.getUid());
        userCredentials.setCredentials(encodePassword(reqVO.getPassword()));
        userCredentials.setExpireTime(LocalDateTime.now().plusYears(99));
        shareUserCredentialsMapper.insert(userCredentials);
        // 插入关联岗位
        if (CollectionUtil.isNotEmpty(reqVO.getPostIds())) {
            userPostMapper.insertBatch(convertList(reqVO.getPostIds(),
                    postId -> new UserPostDO().setUserId(user.getUid()).setPostId(postId)));
        }
        return user.getUid();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateReqVO reqVO) {
        // 校验正确性
        validateUserForCreateOrUpdate(reqVO.getUid(), reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        // 更新用户
        AdminUserInfoDO updateObj = UserCenterConvert.INSTANCE.convert(reqVO);
        adminUserInfoMapper.updateByUid(updateObj);
        // 更新岗位
        updateUserPost(reqVO, updateObj);
    }

    private void updateUserPost(UserUpdateReqVO reqVO, AdminUserInfoDO updateObj) {
        String userId = reqVO.getUid();
        Set<Long> dbPostIds = convertSet(userPostMapper.selectListByUserId(userId), UserPostDO::getPostId);
        // 计算新增和删除的岗位编号
        Set<Long> postIds = reqVO.getPostIds();
        Collection<Long> createPostIds = CollUtil.subtract(postIds, dbPostIds);
        Collection<Long> deletePostIds = CollUtil.subtract(dbPostIds, postIds);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (!CollectionUtil.isEmpty(createPostIds)) {
            userPostMapper.insertBatch(convertList(createPostIds,
                    postId -> new UserPostDO().setUserId(userId).setPostId(postId)));
        }
        if (!CollectionUtil.isEmpty(deletePostIds)) {
            userPostMapper.deleteByUserIdAndPostId(userId, deletePostIds);
        }
    }

    @Override
    public void updateUserLogin(String uid, String loginIp) {
        adminUserInfoMapper.updateByUid(new AdminUserInfoDO()
                .setUid(uid)
                .setLoginDate(LocalDateTime.now())
                .setLoginIp(loginIp));
    }

    @Override
    public void updateUserProfile(String uid, UserProfileUpdateReqVO reqVO) {
        // 校验正确性
        validateUserExists(uid);
        validateEmailUnique(uid, reqVO.getEmail());
        validateMobileUnique(uid, reqVO.getMobile());
        // 执行更新
        adminUserInfoMapper.updateByUid(UserCenterConvert.INSTANCE.convert(reqVO).setUid(uid));
    }

    @Override
    public void updateUserPassword(String uid, UserProfileUpdatePasswordReqVO reqVO) {
        // 校验旧密码密码
        ShareUserCredentialsDO shareUserCredentialsDO = validateOldPassword(uid, reqVO.getOldPassword());
        // 执行更新
        shareUserCredentialsDO.setCredentials(encodePassword(reqVO.getNewPassword()));
        shareUserCredentialsMapper.updateById(shareUserCredentialsDO);
    }

    @Override
    public void updateUserMobileByWeixin(String userId, AppUserUpdateMobileByWeixinReqVO reqVO) {
        // 1.1 获得对应的手机号信息
        SocialWxPhoneNumberInfoRespDTO wxMaPhoneNumberInfo = socialClientApi.getWxMaPhoneNumberInfo(
                UserTypeEnum.MEMBER.getCode(), reqVO.getCode());
        Assert.notNull(wxMaPhoneNumberInfo, "获得手机信息失败，结果为空");
        // 1.2 校验新手机是否已经被绑定
        validateMobileUnique(userId, wxMaPhoneNumberInfo.getPhoneNumber());

        // 2. 更新用户手机
        adminUserInfoMapper.updateById(AdminUserInfoDO.builder().uid(userId).mobile(wxMaPhoneNumberInfo.getPhoneNumber()).build());
    }

    @Override
    public String updateUserAvatar(String uid, InputStream avatarFile) throws Exception {
        validateUserExists(uid);
        // 存储文件
        String avatar = fileApi.createFile(IoUtil.readBytes(avatarFile));
        // 更新路径
        AdminUserInfoDO sysUserDO = new AdminUserInfoDO();
        sysUserDO.setUid(uid);
        sysUserDO.setAvatar(avatar);
        adminUserInfoMapper.updateById(sysUserDO);
        return avatar;
    }

    @Override
    public void updateUserPassword(String uid, String password) {
        // 校验用户存在
        validateUserExists(uid);
        // 更新密码
        ShareUserCredentialsDO updateObj = new ShareUserCredentialsDO();
        updateObj.setUid(uid);
        updateObj.setIdentifier(uid);
        updateObj.setIdentityType(CredentialsIdentifyTypeEnums.PASSWORD);
        updateObj.setStatus(CommonStatusEnum.ENABLE);
        updateObj.setCredentials(encodePassword(password)); // 加密密码
        updateObj.setExpireTime(LocalDateTime.now().plusYears(99));
        shareUserCredentialsMapper.updateByUidAndIdentityType(updateObj);
    }

    @Override
    public void updateUserStatus(String uid, Integer status) {
        // 校验用户存在
        validateUserExists(uid);
        // 更新状态
        AdminUserInfoDO updateObj = new AdminUserInfoDO();
        updateObj.setUid(uid);
        updateObj.setStatus(CommonEnum.of(status, CommonStatusEnum.class));
        adminUserInfoMapper.updateByUid(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String uid) {
        // 校验用户存在
        validateUserExists(uid);
        // 删除用户
        adminUserInfoMapper.delete(new LambdaUpdateWrapperX<AdminUserInfoDO>().eq(AdminUserInfoDO::getUid, uid));
        // 删除用户关联数据
        rolePermissionCoreService.processUserDeleted(uid);
        // 删除用户岗位
        userPostMapper.deleteByUserId(uid);
    }

    @Override
    public AdminUserInfoDO getUserByUsername(String username) {
        return adminUserInfoMapper.selectByUserName(username);
    }

    @Override
    public AdminUserInfoDO getUserByMobile(String mobile) {
        return adminUserInfoMapper.selectByMobile(mobile);
    }

    @Override
    public PageResult<AdminUserInfoDO> getUserPage(UserPageReqVO reqVO) {
        return adminUserInfoMapper.selectPage(reqVO, getDeptCondition(reqVO.getDeptId()));
    }

    @Override
    public AdminUserInfoDO getUser(String uid) {
        return adminUserInfoMapper.selectByUid(uid);
    }

    @Override
    public List<AdminUserInfoDO> getUserListByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return adminUserInfoMapper.selectListByDeptIds(deptIds);
    }

    @Override
    public List<AdminUserInfoDO> getUserListByPostIds(Collection<Long> postIds) {
        if (CollUtil.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        Set<String> userIds = convertSet(userPostMapper.selectListByPostIds(postIds), UserPostDO::getUserId);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return adminUserInfoMapper.selectByBatchUid(Lists.newArrayList(userIds));
    }

    @Override
    public List<AdminUserInfoDO> getUserList(Collection<String> uidList) {
        if (CollUtil.isEmpty(uidList)) {
            return Collections.emptyList();
        }
        return adminUserInfoMapper.selectByBatchUid(Lists.newArrayList(uidList));
    }

    @Override
    public void validateUserList(Collection<String> uidList) {
        if (CollUtil.isEmpty(uidList)) {
            return;
        }
        // 获得岗位信息
        List<AdminUserInfoDO> users = adminUserInfoMapper.selectBatchIds(uidList);
        Map<String, AdminUserInfoDO> userMap = RoakerCollectionUtils.convertMap(users, AdminUserInfoDO::getUid);
        // 校验
        uidList.forEach(uid -> {
            AdminUserInfoDO user = userMap.get(uid);
            if (user == null) {
                throw exception(USER_NOT_EXISTS);
            }
        });
    }

    @Override
    public List<AdminUserInfoDO> getUserList(UserExportReqVO reqVO) {
        return adminUserInfoMapper.selectList(reqVO, getDeptCondition(reqVO.getDeptId()));
    }

    @Override
    public List<AdminUserInfoDO> getUserListByNickname(String nickname) {
        return Lists.newArrayList(adminUserInfoMapper.selectByUserName(nickname));
    }

    /**
     * 获得部门条件：查询指定部门的子部门编号们，包括自身
     *
     * @param deptId 部门编号
     * @return 部门编号集合
     */
    private Set<Long> getDeptCondition(Long deptId) {
        if (deptId == null) {
            return Collections.emptySet();
        }
        Set<Long> deptIds = convertSet(deptService.getChildDeptList(deptId), DeptDO::getId);
        deptIds.add(deptId); // 包括自身
        return deptIds;
    }

    private void validateUserForCreateOrUpdate(String uid, String username, String mobile, String email,
                                               Long deptId, Set<Long> postIds) {
        // 关闭数据权限，避免因为没有数据权限，查询不到数据，进而导致唯一校验不正确
        DataPermissionUtils.executeIgnore(() -> {
            // 校验用户存在
            validateUserExists(uid);
            // 校验用户名唯一
            validateUsernameUnique(uid, username);
            // 校验手机号唯一
            validateMobileUnique(uid, mobile);
            // 校验邮箱唯一
            validateEmailUnique(uid, email);
            // 校验部门处于开启状态
            deptService.validateDeptList(RoakerCollectionUtils.singleton(deptId));
            // 校验岗位处于开启状态
            postService.validatePostList(postIds);
        });
    }

    @VisibleForTesting
    void validateUserExists(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return;
        }
        AdminUserInfoDO user = adminUserInfoMapper.selectByUid(uid);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    void validateUsernameUnique(String uid, String username) {
        if (StringUtils.isBlank(username)) {
            return;
        }
        AdminUserInfoDO user = adminUserInfoMapper.selectByUserName(username);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (StringUtils.isEmpty(uid)) {
            throw exception(USER_USERNAME_EXISTS);
        }
        if (!StringUtils.equals(uid, user.getUid())) {
            throw exception(USER_USERNAME_EXISTS);
        }
    }

    @VisibleForTesting
    void validateEmailUnique(String uid, String email) {
        if (StringUtils.isBlank(email)) {
            return;
        }
        AdminUserInfoDO user = adminUserInfoMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (StringUtils.isEmpty(uid)) {
            throw exception(USER_EMAIL_EXISTS);
        }
        if (!StringUtils.equals(uid, user.getUid())) {
            throw exception(USER_EMAIL_EXISTS);
        }
    }

    @VisibleForTesting
    void validateMobileUnique(String uid, String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return;
        }
        AdminUserInfoDO user = adminUserInfoMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (StringUtils.isEmpty(uid)) {
            throw exception(USER_MOBILE_EXISTS);
        }
        if (!StringUtils.equals(uid, user.getUid())) {
            throw exception(USER_MOBILE_EXISTS);
        }
    }

    /**
     * 校验旧密码
     *
     * @param uid         用户 uid
     * @param oldPassword 旧密码
     */
    @VisibleForTesting
    ShareUserCredentialsDO validateOldPassword(String uid, String oldPassword) {
        ShareUserCredentialsDO shareUserCredentialsDO = shareUserCredentialsMapper.selectByUidAndIdentityType(uid, CredentialsIdentifyTypeEnums.PASSWORD);
        if (shareUserCredentialsDO == null) {
            throw exception(USER_PASSWORD_FAILED);
        }
        if (!isPasswordMatch(oldPassword, shareUserCredentialsDO.getCredentials())) {
            throw exception(USER_PASSWORD_FAILED);
        }
        return shareUserCredentialsDO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public UserImportRespVO importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport) {
        if (CollUtil.isEmpty(importUsers)) {
            throw exception(USER_IMPORT_LIST_IS_EMPTY);
        }
        UserImportRespVO respVO = UserImportRespVO.builder().createUsernames(new ArrayList<>())
                .updateUsernames(new ArrayList<>()).failureUsernames(new LinkedHashMap<>()).build();
        importUsers.forEach(importUser -> {
            // 校验，判断是否有不符合的原因
            try {
                validateUserForCreateOrUpdate(null, null, importUser.getMobile(), importUser.getEmail(),
                        importUser.getDeptId(), null);
            } catch (ServiceException ex) {
                respVO.getFailureUsernames().put(importUser.getUsername(), ex.getMessage());
                return;
            }
            // 判断如果不存在，在进行插入
            AdminUserInfoDO existUser = adminUserInfoMapper.selectByUserName(importUser.getUsername());
            if (existUser == null) {
                AdminUserInfoDO userInfoDO = UserCenterConvert.INSTANCE.convert(importUser)
                        .setPostIds(new HashSet<>());
                adminUserInfoMapper.insert(userInfoDO); // 设置默认密码及空岗位编号数组
                ShareUserCredentialsDO userCredentials = new ShareUserCredentialsDO();
                userCredentials.setStatus(CommonStatusEnum.ENABLE);
                userCredentials.setIdentifier(userInfoDO.getUid());
                userCredentials.setIdentityType(CredentialsIdentifyTypeEnums.PASSWORD);
                userCredentials.setUid(userInfoDO.getUid());
                userCredentials.setCredentials(encodePassword(encodePassword(userInitPassword)));
                userCredentials.setExpireTime(LocalDateTime.now().plusYears(99));
                shareUserCredentialsMapper.insert(userCredentials);
                respVO.getCreateUsernames().add(importUser.getUsername());
                return;
            }
            // 如果存在，判断是否允许更新
            if (!isUpdateSupport) {
                respVO.getFailureUsernames().put(importUser.getUsername(), USER_USERNAME_EXISTS.getMsg());
                return;
            }
            AdminUserInfoDO updateUser = UserCenterConvert.INSTANCE.convert(importUser);
            updateUser.setUid(existUser.getUid());
            adminUserInfoMapper.updateByUid(updateUser);
            respVO.getUpdateUsernames().add(importUser.getUsername());
        });
        return respVO;
    }

    @Override
    public List<AdminUserInfoDO> getUserListByStatus(Integer status) {
        return adminUserInfoMapper.selectListByStatus(CommonEnum.of(status, CommonStatusEnum.class));
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
