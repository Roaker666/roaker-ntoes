package com.roaker.notes.uc.api.oauth2;

import com.roaker.notes.commons.constants.ApplicationNameConstants;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenCheckRespDTO;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenCreateReqDTO;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenRespDTO;
import com.roaker.notes.vo.CommonResult;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Oauth2.0 Token API 接口
 *
 * @author 芋道源码
 */
@FeignClient(name = ApplicationNameConstants.AUTH_NAME, fallbackFactory = Oauth2TokenApiFallback.class, dismiss404 = true)
public interface Oauth2TokenApi {

    /**
     * 创建访问令牌
     *
     * @param reqDTO 访问令牌的创建信息
     * @return 访问令牌的信息
     */
    @PostMapping("/auth/inner-call/create-access-token")
    CommonResult<Oauth2AccessTokenRespDTO> createAccessToken(@RequestBody @Valid Oauth2AccessTokenCreateReqDTO reqDTO);

    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    @PostMapping("/auth/inner-call/check-access-token")
    CommonResult<Oauth2AccessTokenCheckRespDTO> checkAccessToken(String accessToken);

    /**
     * 移除访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    @PostMapping("/auth/inner-call/remove-access-token")
    CommonResult<Oauth2AccessTokenRespDTO> removeAccessToken(String accessToken);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @param clientId     客户端编号
     * @return 访问令牌的信息
     */
    @PostMapping("/auth/inner-call/refresh-access-token")
    CommonResult<Oauth2AccessTokenRespDTO> refreshAccessToken(String refreshToken, String clientId);

}
