package com.roaker.notes.uc.api.oauth2;

import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenCheckRespDTO;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenCreateReqDTO;
import com.roaker.notes.uc.dto.oauth2.Oauth2AccessTokenRespDTO;
import com.roaker.notes.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
@Component
public class Oauth2TokenClientFallback implements FallbackFactory<Oauth2TokenClient> {
    @Override
    public Oauth2TokenClient create(Throwable throwable) {
        return new Oauth2TokenClient() {
            @Override
            public CommonResult<Oauth2AccessTokenRespDTO> createAccessToken(Oauth2AccessTokenCreateReqDTO reqDTO) {
                log.info("Oauth2 Auth client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.AUTH_SERVER_ERROR);
            }

            @Override
            public CommonResult<Oauth2AccessTokenCheckRespDTO> checkAccessToken(String accessToken) {
                log.info("Oauth2 Auth client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.AUTH_SERVER_ERROR);
            }

            @Override
            public CommonResult<Oauth2AccessTokenRespDTO> removeAccessToken(String accessToken) {
                log.info("Oauth2 Auth client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.AUTH_SERVER_ERROR);
            }

            @Override
            public CommonResult<Oauth2AccessTokenRespDTO> refreshAccessToken(String refreshToken, String clientId) {
                log.info("Oauth2 Auth client initiate fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.AUTH_SERVER_ERROR);
            }
        };
    }
}
