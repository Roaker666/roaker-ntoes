package com.roaker.notes.uc.dto;

import com.roaker.notes.enums.CommonStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ShareUserDTO implements Serializable {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 区号
     */
    private String cyCode;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态 {@link CommonStatusEnum}
     */
    private CommonStatusEnum status;

    /**
     * 注册 IP
     */
    private String registerIp;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;
}
