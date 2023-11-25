package com.roaker.notes.uc.service.auth;

import com.roaker.notes.uc.controller.auth.vo.AuthSmsSendReqVO;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface AdminAuthService {
    /**
     * 基于 token 退出登录
     *
     * @param token token
     * @param logType 登出类型
     */
    void logout(String token, Integer logType);

    /**
     * 短信验证码发送
     *
     * @param reqVO 发送请求
     */
    void sendSmsCode(AuthSmsSendReqVO reqVO);
}
