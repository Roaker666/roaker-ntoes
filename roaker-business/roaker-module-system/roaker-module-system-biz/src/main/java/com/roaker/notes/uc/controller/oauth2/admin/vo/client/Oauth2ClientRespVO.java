package com.roaker.notes.uc.controller.oauth2.admin.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - Oauth2 客户端 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Oauth2ClientRespVO extends Oauth2ClientBaseVO {

    @Schema(description = "编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
