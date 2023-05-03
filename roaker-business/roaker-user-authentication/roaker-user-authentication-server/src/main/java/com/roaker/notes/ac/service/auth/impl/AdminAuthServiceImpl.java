package com.roaker.notes.ac.service.auth.impl;

import cn.hutool.core.util.ObjectUtil;
import com.roaker.notes.ac.controller.auth.vo.AuthSmsSendReqVO;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.ac.service.auth.AdminAuthService;
import com.roaker.notes.ac.service.oauth2.Oauth2TokenService;
import com.roaker.notes.uc.api.UserCenterClient;
import com.roaker.notes.uc.dto.ShareUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.roaker.notes.commons.constants.ErrorCodeConstants.AUTH_MOBILE_NOT_EXISTS;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {
    private final Oauth2TokenService oauth2TokenService;
    private final UserCenterClient userCenterClient;
    @Override
    public void logout(String token, Integer logType) {
        oauth2TokenService.removeAccessToken(token);
    }

    @Override
    public void sendSmsCode(AuthSmsSendReqVO reqVO) {
        ShareUserDTO shareUserDTO = userCenterClient.getByMobile(reqVO.getMobile());
        if (shareUserDTO == null) {
            throw exception(AUTH_MOBILE_NOT_EXISTS);
        }
    }
}
