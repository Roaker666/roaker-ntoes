package com.roaker.notes.ac.service.auth.impl;

import cn.hutool.core.util.ObjectUtil;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.ac.service.auth.AdminAuthService;
import com.roaker.notes.ac.service.oauth2.Oauth2TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {
    private final Oauth2TokenService oauth2TokenService;
    @Override
    public void logout(String token, Integer logType) {
        oauth2TokenService.removeAccessToken(token);
    }
}
