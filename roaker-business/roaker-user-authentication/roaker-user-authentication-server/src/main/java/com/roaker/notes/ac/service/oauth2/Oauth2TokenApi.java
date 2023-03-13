package com.roaker.notes.ac.service.oauth2;

import com.roaker.notes.ac.dto.oauth2.Oauth2AccessTokenCheckRespDTO;
import com.roaker.notes.ac.dto.oauth2.Oauth2AccessTokenCreateReqDTO;
import com.roaker.notes.ac.dto.oauth2.Oauth2AccessTokenRespDTO;
import jakarta.validation.Valid;

/**
 * Oauth2.0 Token API 接口
 *
 * @author 芋道源码
 */
public interface Oauth2TokenApi {

    /**
     * 创建访问令牌
     *
     * @param reqDTO 访问令牌的创建信息
     * @return 访问令牌的信息
     */
    Oauth2AccessTokenRespDTO createAccessToken(@Valid Oauth2AccessTokenCreateReqDTO reqDTO);

    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    Oauth2AccessTokenCheckRespDTO checkAccessToken(String accessToken);

    /**
     * 移除访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    Oauth2AccessTokenRespDTO removeAccessToken(String accessToken);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @param clientId     客户端编号
     * @return 访问令牌的信息
     */
    Oauth2AccessTokenRespDTO refreshAccessToken(String refreshToken, String clientId);

}
