package com.roaker.notes.ac.controller.oauth2.admin.vo.client;

import cn.hutool.core.util.StrUtil;
import com.roaker.notes.commons.utils.JacksonUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

/**
 * Oauth2 客户端Base,提供给添加、修改、详细子VO使用
 * 如果子VO有差异时，最好别添加到这里
 * @author lei.rao
 * @since 1.0
 */
@Data
public class Oauth2ClientBaseVO {
    @Schema(description = "客户端编号", required = true, example = "tudou")
    @NotNull(message = "客户端编号不能为空")
    private String clientId;
    @Schema(description = "客户端密钥", required = true, example = "fan")
    @NotNull(message = "客户端密钥不能为空")
    private String secret;
    @Schema(description = "应用名", required = true, example = "土豆")
    @NotNull(message = "应用名不能为空")
    private String name;
    @Schema(description = "应用图标", required = true, example = "https://www.roaker.com/xx.png")
    @NotNull(message = "应用图标不能为空")
    @URL(message = "应用图标的地址不正确")
    private String logo;
    @Schema(description = "应用描述", example = "我是一个应用")
    private String description;
    @Schema(description = "状态,参见 CommonStatusEnum 枚举", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;
    @Schema(description = "访问令牌的有效期", required = true, example = "8640")
    @NotNull(message = "访问令牌的有效期不能为空")
    private Integer accessTokenValiditySeconds;
    @Schema(description = "刷新令牌的有效期", required = true, example = "8640000")
    @NotNull(message = "刷新令牌的有效期不能为空")
    private Integer refreshTokenValiditySeconds;
    @Schema(description = "可重定向的 URI 地址", required = true, example = "https://www.roaker.com")
    @NotNull(message = "可重定向的 URI 地址不能为空")
    private List<@NotEmpty(message = "重定向的 URI 不能为空")
    @URL(message = "重定向的 URI 格式不正确") String> redirectUris;
    @Schema(description = "授权类型,参见 Oauth2GrantTypeEnum 枚举", required = true, example = "password")
    @NotNull(message = "授权类型不能为空")
    private List<String> authorizedGrantTypes;
    @Schema(description = "授权范围", example = "user_info")
    private List<String> scopes;
    @Schema(description = "自动通过的授权范围", example = "user_info")
    private List<String> autoApproveScopes;
    @Schema(description = "权限", example = "system:user:query")
    private List<String> authorities;
    @Schema(description = "资源", example = "1024")
    private List<String> resourceIds;
    @Schema(description = "附加信息", example = "{yunai: true}")
    private String additionalInformation;
    @AssertTrue(message = "附加信息必须是 JSON 格式")
    public boolean isAdditionalInformationJson() {
        return StrUtil.isEmpty(additionalInformation) || JacksonUtils.isJson(additionalInformation);
    }
}
