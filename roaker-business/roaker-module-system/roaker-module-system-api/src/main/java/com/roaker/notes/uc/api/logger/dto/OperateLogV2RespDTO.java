package com.roaker.notes.uc.api.logger.dto;

import com.roaker.notes.enums.UserTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 系统操作日志 Resp DTO
 *
 * @author HUIHUI
 */
@Data
@Accessors(chain = true)
public class OperateLogV2RespDTO {

    /**
     * 链路追踪编号
     */
    private String traceId;
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    /**
     * 操作模块类型
     */
    private String type;
    /**
     * 操作名
     */
    private String subType;
    /**
     * 操作模块业务编号
     */
    private Long bizId;
    /**
     * 操作内容
     */
    private String action;
    /**
     * 拓展字段
     */
    private String extra;

    /**
     * 请求方法名
     */
    private String requestMethod;
    /**
     * 请求地址
     */
    private String requestUrl;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 浏览器 UA
     */
    private String userAgent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
