package com.roaker.notes.uc.service.oauth2.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.roaker.notes.uc.controller.oauth2.admin.vo.token.Oauth2AccessTokenPageReqVO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2RefreshTokenDO;
import com.roaker.notes.uc.dal.mapper.oauth2.Oauth2AccessTokenMapper;
import com.roaker.notes.uc.dal.mapper.oauth2.Oauth2RefreshTokenMapper;
import com.roaker.notes.uc.service.oauth2.Oauth2ClientService;
import com.roaker.notes.uc.service.oauth2.Oauth2TokenService;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.utils.date.DateUtils;
import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.convertSet;
import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception0;

/**
 * Oauth2.0 Token Service 实现类
 *
 * @author lei.rao
 */
@Service
public class Oauth2TokenServiceImpl implements Oauth2TokenService {

    @Resource
    private Oauth2AccessTokenMapper oauth2AccessTokenMapper;
    @Resource
    private Oauth2RefreshTokenMapper oauth2RefreshTokenMapper;
//
//    @Resource
//    private Oauth2AccessTokenRedisDAO oauth2AccessTokenRedisDAO;

    @Resource
    private Oauth2ClientService oauth2ClientService;

    @Override
    @Transactional
    public Oauth2AccessTokenDO createAccessToken(String userId, Integer userType, String clientId, List<String> scopes) {
        Oauth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        // 创建刷新令牌
        Oauth2RefreshTokenDO refreshTokenDO = createOauth2RefreshToken(userId, userType, clientDO, scopes);
        // 创建访问令牌
        return createOauth2AccessToken(refreshTokenDO, clientDO);
    }

    @Override
    public Oauth2AccessTokenDO refreshAccessToken(String refreshToken, String clientId) {
        // 查询访问令牌
        Oauth2RefreshTokenDO refreshTokenDO = oauth2RefreshTokenMapper.selectByRefreshToken(refreshToken);
        if (refreshTokenDO == null) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "无效的刷新令牌");
        }

        // 校验 Client 匹配
        Oauth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        if (ObjectUtil.notEqual(clientId, refreshTokenDO.getClientId())) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "刷新令牌的客户端编号不正确");
        }

        // 移除相关的访问令牌
        List<Oauth2AccessTokenDO> accessTokenDOs = oauth2AccessTokenMapper.selectListByRefreshToken(refreshToken);
        if (CollUtil.isNotEmpty(accessTokenDOs)) {
            oauth2AccessTokenMapper.deleteBatchIds(convertSet(accessTokenDOs, Oauth2AccessTokenDO::getId));
//            oauth2AccessTokenRedisDAO.deleteList(convertSet(accessTokenDOs, Oauth2AccessTokenDO::getAccessToken));
        }

        // 已过期的情况下，删除刷新令牌
        if (DateUtils.isExpired(refreshTokenDO.getExpiresTime())) {
            oauth2RefreshTokenMapper.deleteById(refreshTokenDO.getId());
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "刷新令牌已过期");
        }

        // 创建访问令牌
        return createOauth2AccessToken(refreshTokenDO, clientDO);
    }

    @Override
    public Oauth2AccessTokenDO getAccessToken(String accessToken) {
        // 优先从 Redis 中获取
//        Oauth2AccessTokenDO accessTokenDO = oauth2AccessTokenRedisDAO.get(accessToken);
//        if (accessTokenDO != null) {
//            return accessTokenDO;
//        }

        // 获取不到，从 MySQL 中获取
        Oauth2AccessTokenDO accessTokenDO = oauth2AccessTokenMapper.selectByAccessToken(accessToken);
        // 如果在 MySQL 存在，则往 Redis 中写入
        if (accessTokenDO != null && !DateUtils.isExpired(accessTokenDO.getExpiresTime())) {
//            oauth2AccessTokenRedisDAO.set(accessTokenDO);
        }
        return accessTokenDO;
    }

    @Override
    public Oauth2AccessTokenDO checkAccessToken(String accessToken) {
        Oauth2AccessTokenDO accessTokenDO = getAccessToken(accessToken);
        if (accessTokenDO == null) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌不存在");
        }
        if (DateUtils.isExpired(accessTokenDO.getExpiresTime())) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌已过期");
        }
        return accessTokenDO;
    }

    @Override
    public Oauth2AccessTokenDO removeAccessToken(String accessToken) {
        // 删除访问令牌
        Oauth2AccessTokenDO accessTokenDO = oauth2AccessTokenMapper.selectByAccessToken(accessToken);
        if (accessTokenDO == null) {
            return null;
        }
        oauth2AccessTokenMapper.deleteById(accessTokenDO.getId());
//        oauth2AccessTokenRedisDAO.delete(accessToken);
        // 删除刷新令牌
        oauth2RefreshTokenMapper.deleteByRefreshToken(accessTokenDO.getRefreshToken());
        return accessTokenDO;
    }

    @Override
    public PageResult<Oauth2AccessTokenDO> getAccessTokenPage(Oauth2AccessTokenPageReqVO  reqVO) {
        return oauth2AccessTokenMapper.selectPage(reqVO);
    }

    private Oauth2AccessTokenDO createOauth2AccessToken(Oauth2RefreshTokenDO refreshTokenDO, Oauth2ClientDO clientDO) {
        Oauth2AccessTokenDO accessTokenDO = new Oauth2AccessTokenDO().setAccessToken(generateAccessToken())
                .setUserId(refreshTokenDO.getUserId()).setUserType(refreshTokenDO.getUserType())
                .setClientId(clientDO.getClientId()).setScopes(refreshTokenDO.getScopes())
                .setRefreshToken(refreshTokenDO.getRefreshToken())
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getAccessTokenValiditySeconds()));
        oauth2AccessTokenMapper.insert(accessTokenDO);
        // 记录到 Redis 中
//        oauth2AccessTokenRedisDAO.set(accessTokenDO);
        return accessTokenDO;
    }

    private Oauth2RefreshTokenDO createOauth2RefreshToken(String userId, Integer userType, Oauth2ClientDO clientDO, List<String> scopes) {
        Oauth2RefreshTokenDO refreshToken = new Oauth2RefreshTokenDO().setRefreshToken(generateRefreshToken())
                .setUserId(userId).setUserType(UserTypeEnum.valueOf(userType))
                .setClientId(clientDO.getClientId()).setScopes(scopes)
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getRefreshTokenValiditySeconds()));
        oauth2RefreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    private static String generateAccessToken() {
        return IdUtil.fastSimpleUUID();
    }

    private static String generateRefreshToken() {
        return IdUtil.fastSimpleUUID();
    }

}
