package com.roaker.notes.uc.vo.user;

import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.SocialTypeEnum;
import com.roaker.notes.uc.controller.dept.vo.dept.DeptSimpleRespVO;
import com.roaker.notes.uc.controller.dept.vo.post.PostSimpleRespVO;
import com.roaker.notes.uc.vo.permission.RoleSimpleRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台 - 用户个人中心信息 Response VO")
public class UserProfileRespVO extends UserBaseVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private CommonStatusEnum status;

    @Schema(description = "最后登录 IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "192.168.1.1")
    private String loginIp;

    @Schema(description = "最后登录时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime loginDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;
    /**
     * 所属角色
     */
    private List<RoleSimpleRespVO> roles;
    /**
     * 所在部门
     */
    private DeptSimpleRespVO dept;
    /**
     * 所属岗位数组
     */
    private List<PostSimpleRespVO> posts;
    /**
     * 社交用户数组
     */
    private List<SocialUser> socialUsers;

    @Schema(description = "社交用户")
    @Data
    public static class SocialUser {

        @Schema(description = "社交平台的类型，参见 SocialTypeEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
        private Integer type;

        @Schema(description = "社交用户的 openid", requiredMode = Schema.RequiredMode.REQUIRED, example = "IPRmJ0wvBptiPIlGEZiPewGwiEiE")
        private String openid;

    }

}
