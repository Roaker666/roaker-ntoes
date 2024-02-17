package com.roaker.notes.uc.service.user.impl;

import com.roaker.notes.uc.api.user.AdminUserApi;
import com.roaker.notes.uc.converter.user.UserCenterConvert;
import com.roaker.notes.uc.dal.dataobject.user.AdminUserInfoDO;
import com.roaker.notes.uc.service.user.AdminUserService;
import com.roaker.notes.uc.vo.user.AdminUserRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService userService;

    @Override
    public AdminUserRespDTO getUser(String uid) {
        AdminUserInfoDO user = userService.getUser(uid);
        return UserCenterConvert.INSTANCE.convert4(user);
    }

    @Override
    public List<AdminUserRespDTO> getUserList(Collection<String> uidList) {
        List<AdminUserInfoDO> users = userService.getUserList(uidList);
        return UserCenterConvert.INSTANCE.convertList4(users);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds) {
        List<AdminUserInfoDO> users = userService.getUserListByDeptIds(deptIds);
        return UserCenterConvert.INSTANCE.convertList4(users);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds) {
        List<AdminUserInfoDO> users = userService.getUserListByPostIds(postIds);
        return UserCenterConvert.INSTANCE.convertList4(users);
    }

    @Override
    public void validateUserList(Collection<String> uidList) {
        userService.validateUserList(uidList);
    }

}
