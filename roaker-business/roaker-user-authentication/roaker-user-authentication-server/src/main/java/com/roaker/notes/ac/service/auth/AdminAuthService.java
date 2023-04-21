package com.roaker.notes.ac.service.auth;

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
}
