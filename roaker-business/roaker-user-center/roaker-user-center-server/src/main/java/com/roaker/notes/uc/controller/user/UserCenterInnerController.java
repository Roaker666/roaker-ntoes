package com.roaker.notes.uc.controller.user;

import com.roaker.notes.uc.api.user.UserCenterClient;
import com.roaker.notes.uc.converter.user.UserCenterConvert;
import com.roaker.notes.uc.dto.user.ShareUserDTO;
import com.roaker.notes.uc.dto.user.SocialUserBindReqDTO;
import com.roaker.notes.uc.dto.user.SocialUserUnbindReqDTO;
import com.roaker.notes.uc.service.user.SocialUserService;
import com.roaker.notes.uc.service.user.UserCenterService;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "用户中心 —— user center API")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserCenterInnerController implements UserCenterClient {
    private final UserCenterService userCenterService;

    private final SocialUserService socialUserService;

    @Override
    public ShareUserDTO getByMobile(String mobile) {
        return UserCenterConvert.INSTANCE.convert(userCenterService.getByMobile(mobile));
    }

    @Override
    public CommonResult<String> getAuthorizeUrl(Integer type, String redirectUri) {
        return CommonResult.success(socialUserService.getAuthorizeUrl(type, redirectUri));
    }

    @Override
    public CommonResult<Void> bindSocialUser(SocialUserBindReqDTO reqDTO) {
        socialUserService.bindSocialUser(reqDTO);
        return CommonResult.success(null);
    }

    @Override
    public CommonResult<Void> unbindSocialUser(SocialUserUnbindReqDTO reqDTO) {
        socialUserService.unbindSocialUser(reqDTO.getUserId(), reqDTO.getUserType(),
                reqDTO.getType(), reqDTO.getUnionId());
        return CommonResult.success(null);
    }

    @Override
    public CommonResult<String> getBindUserId(Integer userType, Integer type, String code, String state) {
        return CommonResult.success(socialUserService.getBindUserId(userType, type, code, state));
    }
}
