package com.roaker.notes.uc.api.user;

import com.roaker.notes.exception.ServerException;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.uc.dto.user.ShareUserDTO;
import com.roaker.notes.uc.dto.user.SocialUserBindReqDTO;
import com.roaker.notes.uc.dto.user.SocialUserUnbindReqDTO;
import com.roaker.notes.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCenterClientFallback implements FallbackFactory<UserCenterClient> {
    @Override
    public UserCenterClient create(Throwable throwable) {
        return new UserCenterClient() {
            @Override
            public ShareUserDTO getByMobile(String mobile) {
                log.info("User-Center client getByMobile fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.USER_SERVER_ERROR);
            }

            @Override
            public CommonResult<String> getAuthorizeUrl(Integer type, String redirectUri) {
                log.info("User-Center client getAuthorizeUrl fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.USER_SERVER_ERROR);
            }

            @Override
            public CommonResult<Void> bindSocialUser(SocialUserBindReqDTO reqDTO) {
                log.info("User-Center client bindSocialUser fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.USER_SERVER_ERROR);
            }

            @Override
            public CommonResult<Void> unbindSocialUser(SocialUserUnbindReqDTO reqDTO) {
                log.info("User-Center client unbindSocialUser fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.USER_SERVER_ERROR);
            }

            @Override
            public CommonResult<String> getBindUserId(Integer userType, Integer type, String code, String state) {
                log.info("User-Center client getBindUserId fallback caused by :{}", throwable.getMessage());
                throw new ServerException(GlobalErrorCodeConstants.USER_SERVER_ERROR);
            }
        };
    }
}
