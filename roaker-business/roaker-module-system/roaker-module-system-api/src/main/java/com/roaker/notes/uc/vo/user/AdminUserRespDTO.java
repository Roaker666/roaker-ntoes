package com.roaker.notes.uc.vo.user;

import com.roaker.notes.enums.CommonStatusEnum;
import lombok.Data;

import java.util.Set;

/**
 * Admin 用户 Response DTO
 *
 * @author Roaker
 */
@Data
public class AdminUserRespDTO {

    /**
     * 用户ID
     */
    private String uid;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private CommonStatusEnum status;

    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 岗位编号数组
     */
    private Set<Long> postIds;
    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户头像
     */
    private String avatar;

}
