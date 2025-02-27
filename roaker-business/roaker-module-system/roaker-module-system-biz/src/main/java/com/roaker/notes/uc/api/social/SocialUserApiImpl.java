package com.roaker.notes.uc.api.social;

import com.roaker.notes.uc.api.social.dto.SocialUserBindReqDTO;
import com.roaker.notes.uc.api.social.dto.SocialUserRespDTO;
import com.roaker.notes.uc.api.social.dto.SocialUserUnbindReqDTO;
import com.roaker.notes.uc.service.user.SocialUserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

/**
 * 社交用户的 API 实现类
 *
 * @author Roaker
 */
@Service
@Validated
public class SocialUserApiImpl implements SocialUserApi {

    @Resource
    private SocialUserService socialUserService;

    @Override
    public String bindSocialUser(SocialUserBindReqDTO reqDTO) {
        return socialUserService.bindSocialUser(reqDTO);
    }

    @Override
    public void unbindSocialUser(SocialUserUnbindReqDTO reqDTO) {
        socialUserService.unbindSocialUser(reqDTO.getUserId(), reqDTO.getUserType(),
                reqDTO.getSocialType(), reqDTO.getOpenid());
    }

    @Override
    public SocialUserRespDTO getSocialUserByUserId(Integer userType, String userId, Integer socialType) {
        return socialUserService.getSocialUserByUserId(userType, userId, socialType);
    }

    @Override
    public SocialUserRespDTO getSocialUserByCode(Integer userType, Integer socialType, String code, String state) {
       return socialUserService.getSocialUserByCode(userType, socialType, code, state);
    }

}
