package com.roaker.notes.uc.api.user;

import com.roaker.notes.commons.constants.ApplicationNameConstants;
import com.roaker.notes.enums.SocialTypeEnum;
import com.roaker.notes.exception.ServiceException;
import com.roaker.notes.uc.dto.user.ShareUserDTO;
import com.roaker.notes.uc.dto.user.SocialUserBindReqDTO;
import com.roaker.notes.uc.dto.user.SocialUserUnbindReqDTO;
import com.roaker.notes.vo.CommonResult;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = ApplicationNameConstants.UC_NAME, fallbackFactory = UserCenterClientFallback.class, dismiss404 = true)
public interface UserCenterClient {
    @GetMapping("/uc-inner/getUser/{mobile}")
    ShareUserDTO getByMobile(@PathVariable String mobile);

    /**
     * 获得社交平台的授权 URL
     *
     * @param type 社交平台的类型 {@link SocialTypeEnum}
     * @param redirectUri 重定向 URL
     * @return 社交平台的授权 URL
     */
    @GetMapping("/uc-inner/social/getAuthorizeUrl")
    CommonResult<String> getAuthorizeUrl(Integer type, String redirectUri);

    /**
     * 绑定社交用户
     *
     * @param reqDTO 绑定信息
     */
    @GetMapping("/uc-inner/social/bind")
    CommonResult<Void> bindSocialUser(@Valid SocialUserBindReqDTO reqDTO);

    /**
     * 取消绑定社交用户
     *
     * @param reqDTO 解绑
     */
    @PostMapping("/uc-inner/social/unbind")
    CommonResult<Void> unbindSocialUser(@Valid SocialUserUnbindReqDTO reqDTO);

    /**
     * 获得社交用户的绑定用户编号
     * 注意，返回的是 MemberUser 或者 AdminUser 的 id 编号！
     * 在认证信息不正确的情况下，也会抛出 {@link ServiceException} 业务异常
     *
     * @param userType 用户类型
     * @param type 社交平台的类型
     * @param code 授权码
     * @param state state
     * @return 绑定用户编号
     */
    @GetMapping("/uc-inner/social/getBindUserId")
    CommonResult<String> getBindUserId(Integer userType, Integer type, String code, String state);
}
