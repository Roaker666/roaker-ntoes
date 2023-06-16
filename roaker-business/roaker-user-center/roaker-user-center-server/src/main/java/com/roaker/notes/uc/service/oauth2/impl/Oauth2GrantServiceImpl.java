package com.roaker.notes.uc.service.oauth2.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2CodeDO;
import com.roaker.notes.uc.service.oauth2.Oauth2CodeService;
import com.roaker.notes.uc.service.oauth2.Oauth2GrantService;
import com.roaker.notes.uc.service.oauth2.Oauth2TokenService;
import com.roaker.notes.commons.constants.ErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * Oauth2 授予 Service 实现类
 *
 * @author lei.rao
 */
@Service
public class Oauth2GrantServiceImpl implements Oauth2GrantService {

    @Resource
    private Oauth2TokenService oauth2TokenService;
    @Resource
    private Oauth2CodeService oauth2CodeService;
//    @Resource
//    private AdminAuthService adminAuthService;

    @Override
    public Oauth2AccessTokenDO grantImplicit(String userId, Integer userType,
                                             String clientId, List<String> scopes) {
        return oauth2TokenService.createAccessToken(userId, userType, clientId, scopes);
    }

    @Override
    public String grantAuthorizationCodeForCode(String userId, Integer userType,
                                                String clientId, List<String> scopes,
                                                String redirectUri, String state) {
        return oauth2CodeService.createAuthorizationCode(userId, userType, clientId, scopes,
                redirectUri, state).getCode();
    }

    @Override
    public Oauth2AccessTokenDO grantAuthorizationCodeForAccessToken(String clientId, String code,
                                                                    String redirectUri, String state) {
        Oauth2CodeDO codeDO = oauth2CodeService.consumeAuthorizationCode(code);
        Assert.notNull(codeDO, "授权码不能为空"); // 防御性编程
        // 校验 clientId 是否匹配
        if (!StrUtil.equals(clientId, codeDO.getClientId())) {
            throw exception(ErrorCodeConstants.OAUTH2_GRANT_CLIENT_ID_MISMATCH);
        }
        // 校验 redirectUri 是否匹配
        if (!StrUtil.equals(redirectUri, codeDO.getRedirectUri())) {
            throw exception(ErrorCodeConstants.OAUTH2_GRANT_REDIRECT_URI_MISMATCH);
        }
        // 校验 state 是否匹配
        state = StrUtil.nullToDefault(state, ""); // 数据库 state 为 null 时，会设置为 "" 空串
        if (!StrUtil.equals(state, codeDO.getState())) {
            throw exception(ErrorCodeConstants.OAUTH2_GRANT_STATE_MISMATCH);
        }

        // 创建访问令牌
        return oauth2TokenService.createAccessToken(codeDO.getUserId(), codeDO.getUserType().getCode(),
                codeDO.getClientId(), codeDO.getScopes());
    }

    @Override
    public Oauth2AccessTokenDO grantPassword(String username, String password, String clientId, List<String> scopes) {
//        // 使用账号 + 密码进行登录
//        AdminUserDO user = adminAuthService.authenticate(username, password);
//        Assert.notNull(user, "用户不能为空！"); // 防御性编程
//
//        // 创建访问令牌
//        return oauth2TokenService.createAccessToken(user.getId(), UserTypeEnum.ADMIN.getValue(), clientId, scopes);
        return null;
    }

    @Override
    public Oauth2AccessTokenDO grantRefreshToken(String refreshToken, String clientId) {
        return oauth2TokenService.refreshAccessToken(refreshToken, clientId);
    }

    @Override
    public Oauth2AccessTokenDO grantClientCredentials(String clientId, List<String> scopes) {
        // TODO 芋艿：项目中使用 Oauth2 解决的是三方应用的授权，内部的 SSO 等问题，所以暂时不考虑 client_credentials 这个场景
        throw new UnsupportedOperationException("暂时不支持 client_credentials 授权模式");
    }

    @Override
    public boolean revokeToken(String clientId, String accessToken) {
        // 先查询，保证 clientId 时匹配的
        Oauth2AccessTokenDO accessTokenDO = oauth2TokenService.getAccessToken(accessToken);
        if (accessTokenDO == null || ObjectUtil.notEqual(clientId, accessTokenDO.getClientId())) {
            return false;
        }
        // 再删除
        return oauth2TokenService.removeAccessToken(accessToken) != null;
    }

}
