package com.roaker.notes.ac.controller.oauth2.inner;

import com.roaker.notes.ac.api.oauth2.Oauth2TokenApi;
import com.roaker.notes.ac.api.oauth2.dto.Oauth2AccessTokenCheckRespDTO;
import com.roaker.notes.ac.api.oauth2.dto.Oauth2AccessTokenCreateReqDTO;
import com.roaker.notes.ac.api.oauth2.dto.Oauth2AccessTokenRespDTO;
import com.roaker.notes.ac.converter.oauth2.Oauth2TokenConvert;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.ac.service.oauth2.Oauth2TokenService;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lei.rao
 * @since 1.0
 */
@RestController
@Tag(name = "认证中心 —— access token API")
@Validated
@Slf4j
public class Oauth2TokenController implements Oauth2TokenApi {
    @Resource
    private Oauth2TokenService oauth2TokenService;

    @Override
    public CommonResult<Oauth2AccessTokenRespDTO> createAccessToken(Oauth2AccessTokenCreateReqDTO reqDTO) {
        Oauth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                reqDTO.getUserId(),
                reqDTO.getUserType(),
                reqDTO.getClientId(),
                reqDTO.getScopes());
        return CommonResult.success(Oauth2TokenConvert.INSTANCE.convert2(accessTokenDO));
    }

    @Override
    public CommonResult<Oauth2AccessTokenCheckRespDTO> checkAccessToken(String accessToken) {
        return CommonResult.success(Oauth2TokenConvert.INSTANCE.convert(oauth2TokenService.checkAccessToken(accessToken)));
    }

    @Override
    public CommonResult<Oauth2AccessTokenRespDTO> removeAccessToken(String accessToken) {
        Oauth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(accessToken);
        return CommonResult.success(Oauth2TokenConvert.INSTANCE.convert2(accessTokenDO));
    }

    @Override
    public CommonResult<Oauth2AccessTokenRespDTO> refreshAccessToken(String refreshToken, String clientId) {
        Oauth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, clientId);
        return CommonResult.success(Oauth2TokenConvert.INSTANCE.convert2(accessTokenDO));
    }
}
