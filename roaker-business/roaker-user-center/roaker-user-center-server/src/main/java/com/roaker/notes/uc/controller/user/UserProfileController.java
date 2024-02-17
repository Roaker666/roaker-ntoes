package com.roaker.notes.uc.controller.user;

import cn.hutool.core.collection.CollUtil;
import com.roaker.notes.datapermission.core.annotation.DataPermission;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.converter.user.UserCenterConvert;
import com.roaker.notes.uc.dal.dataobject.dept.DeptDO;
import com.roaker.notes.uc.dal.dataobject.dept.PostDO;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.dal.dataobject.user.SocialUserDO;
import com.roaker.notes.uc.service.dept.DeptService;
import com.roaker.notes.uc.service.dept.PostService;
import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.service.permission.RoleService;
import com.roaker.notes.uc.service.user.AdminUserService;
import com.roaker.notes.uc.service.user.SocialUserService;
import com.roaker.notes.uc.vo.user.UserProfileRespVO;
import com.roaker.notes.uc.vo.user.UserProfileUpdatePasswordReqVO;
import com.roaker.notes.uc.vo.user.UserProfileUpdateReqVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;

import java.util.List;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.FILE_IS_EMPTY;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;
import static com.roaker.notes.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "管理后台 - 用户个人中心")
@RestController
@RequestMapping("/system/user/profile")
@Validated
@Slf4j
public class UserProfileController {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private RolePermissionCoreService rolePermissionCoreService;
    @Resource
    private RoleService roleService;
    @Resource
    private SocialUserService socialService;

    @GetMapping("/get")
    @Operation(summary = "获得登录用户信息")
    @DataPermission(enable = false) // 关闭数据权限，避免只查看自己时，查询不到部门。
    public CommonResult<UserProfileRespVO> getUserProfile() {
        // 获得用户基本信息
        AdminUserInfoDO user = userService.getUser(getLoginUserId());
        // 获得用户角色
        List<RoleInfoDO> userRoles = roleService.getRoleListFromCache(rolePermissionCoreService.getUserRoleIdListByUserId(user.getUid()));
        // 获得部门信息
        DeptDO dept = user.getDeptId() != null ? deptService.getDept(user.getDeptId()) : null;
        // 获得岗位信息
        List<PostDO> posts = CollUtil.isNotEmpty(user.getPostIds()) ? postService.getPostList(user.getPostIds()) : null;
        // 获得社交用户信息
        List<SocialUserDO> socialUsers = socialService.getSocialUserList(user.getUid(), UserTypeEnum.ADMIN.getCode());
        return success(UserCenterConvert.INSTANCE.convert(user, userRoles, dept, posts, socialUsers));
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户个人信息")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileUpdateReqVO reqVO) {
        userService.updateUserProfile(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "修改用户个人密码")
    public CommonResult<Boolean> updateUserProfilePassword(@Valid @RequestBody UserProfileUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(getLoginUserId(), reqVO);
        return success(true);
    }

    @RequestMapping(value = "/update-avatar",
            method = {RequestMethod.POST, RequestMethod.PUT}) // 解决 uni-app 不支持 Put 上传文件的问题
    @Operation(summary = "上传用户个人头像")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String avatar = userService.updateUserAvatar(getLoginUserId(), file.getInputStream());
        return success(avatar);
    }

}
