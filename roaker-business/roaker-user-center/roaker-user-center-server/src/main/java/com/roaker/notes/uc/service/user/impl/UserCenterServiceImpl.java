package com.roaker.notes.uc.service.user.impl;

import com.roaker.notes.uc.dal.dataobject.user.ShareUserInfoDO;
import com.roaker.notes.uc.dal.mapper.user.ShareUserInfoMapper;
import com.roaker.notes.uc.service.user.UserCenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCenterServiceImpl implements UserCenterService {
    private final ShareUserInfoMapper shareUserInfoMapper;
    @Override
    public ShareUserInfoDO getByMobile(String mobile) {
        return shareUserInfoMapper.selectByMobile(mobile);
    }
}
