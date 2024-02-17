package com.roaker.notes.uc.api.logger.dto;

import com.roaker.notes.enums.LoginLogTypeEnum;
import com.roaker.notes.enums.UserTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 登录日志创建 Request DTO
 *
 * @author Roaker源码
 */
@Data
public class LoginLogCreateReqDTO {

    /**
     * 日志类型
     */
    @NotNull(message = "日志类型不能为空")
    private LoginLogTypeEnum logType;
    /**
     * 链路追踪编号
     */
    private String traceId;

    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    private UserTypeEnum userType;
    /**
     * 用户账号
     *
     * 不再强制校验 username 非空，因为 Member 社交登录时，此时暂时没有 username(mobile）！
     */
    private String username;

    /**
     * 登录结果
     */
    @NotNull(message = "登录结果不能为空")
    private Integer result;

    /**
     * 用户 IP
     */
    @NotEmpty(message = "用户 IP 不能为空")
    private String userIp;
    /**
     * 浏览器 UserAgent
     *
     * 允许空，原因：Job 过期登出时，是无法传递 UserAgent 的
     */
    private String userAgent;

}
