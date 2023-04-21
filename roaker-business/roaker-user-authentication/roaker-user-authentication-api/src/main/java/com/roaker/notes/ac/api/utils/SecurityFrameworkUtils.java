package com.roaker.notes.ac.api.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * @author lei.rao
 * @since 1.0
 */
public class SecurityFrameworkUtils {

    public static final String AUTHORIZATION_BEARER = "Bearer";

    private SecurityFrameworkUtils() {
    }

    /**
     * 从请求中，获得认证 Token
     *
     * @param request 请求
     * @param header  认证 Token 对应的 Header 名字
     * @return 认证 Token
     */
    public static String obtainAuthorization(HttpServletRequest request, String header) {
        String authorization = request.getHeader(header);
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        int index = authorization.indexOf(AUTHORIZATION_BEARER + " ");
        if (index == -1) { // 未找到
            return null;
        }
        return authorization.substring(index + 7).trim();
    }

}