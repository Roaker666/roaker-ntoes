package com.roaker.notes.ac.controller.admin.oauth2.vo.client;

import com.roaker.notes.commons.db.core.dataobject.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - Oauth2 客户端分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Oauth2ClientPageReqVO extends PageParam {

    @Schema(description = "应用名,模糊匹配", example = "用户中心")
    private String name;

    @Schema(description = "状态,参见 CommonStatusEnum 枚举", example = "1")
    private Integer status;

}
