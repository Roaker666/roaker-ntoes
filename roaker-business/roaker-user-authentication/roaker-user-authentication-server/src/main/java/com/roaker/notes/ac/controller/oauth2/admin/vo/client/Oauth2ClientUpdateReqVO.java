package com.roaker.notes.ac.controller.oauth2.admin.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Schema(description = "管理后台 - Oauth2 客户端更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Oauth2ClientUpdateReqVO extends Oauth2ClientBaseVO {

    @Schema(description = "编号", required = true, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

}
