package com.roaker.notes.uc.controller.user;

import com.roaker.notes.enums.UserTypeEnum;
import com.roaker.notes.uc.converter.user.UserCenterConvert;
import com.roaker.notes.uc.service.user.SocialUserService;
import com.roaker.notes.uc.vo.user.SocialUserBindReqVO;
import com.roaker.notes.uc.vo.user.SocialUserUnbindReqVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.roaker.notes.commons.web.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 社交用户")
@RestController
@RequestMapping
@Validated
public class SocialUserController {

    @Resource
    private SocialUserService socialUserService;

    @PostMapping("/system/social-user/bind")
    @Operation(summary = "社交绑定，使用 code 授权码")
    public CommonResult<Boolean> socialBind(@RequestBody @Valid SocialUserBindReqVO reqVO) {
        socialUserService.bindSocialUser(UserCenterConvert.INSTANCE.convert(getLoginUserId(), UserTypeEnum.ADMIN.getCode(), reqVO));
        return CommonResult.success(true);
    }

    @DeleteMapping("/unbind")
    @Operation(summary = "取消社交绑定")
    public CommonResult<Boolean> socialUnbind(@RequestBody SocialUserUnbindReqVO reqVO) {
        socialUserService.unbindSocialUser(getLoginUserId(), UserTypeEnum.ADMIN.getCode(), reqVO.getType(), reqVO.getOpenid());
        return CommonResult.success(true);
    }
}
