package com.roaker.notes.uc.vo.user;

import com.roaker.notes.enums.SocialTypeEnum;
import com.roaker.notes.commons.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "用户 APP - 取消社交绑定 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSocialUserUnbindReqVO {

    @Schema(description = "社交平台的类型,参见 SysUserSocialTypeEnum 枚举值", required = true, example = "10")
    @NotNull(message = "社交平台的类型不能为空")
    private Integer type;

    @Schema(description = "社交用户的 openid", required = true, example = "IPRmJ0wvBptiPIlGEZiPewGwiEiE")
    @NotEmpty(message = "社交用户的 openid 不能为空")
    private String openid;

}
