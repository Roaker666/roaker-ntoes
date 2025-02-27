package com.roaker.notes.uc.controller.oauth2.admin.vo.token;

import com.roaker.notes.commons.db.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 访问令牌分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class Oauth2AccessTokenPageReqVO extends PageParam {

    @Schema(description = "用户编号", required = true, example = "666")
    private String userId;

    @Schema(description = "用户类型,参见 UserTypeEnum 枚举", required = true, example = "2")
    private Integer userType;

    @Schema(description = "客户端编号", required = true, example = "2")
    private String clientId;

}
