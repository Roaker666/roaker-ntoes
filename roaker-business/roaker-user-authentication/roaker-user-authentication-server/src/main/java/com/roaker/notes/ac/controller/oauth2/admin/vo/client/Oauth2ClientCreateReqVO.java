package com.roaker.notes.ac.controller.oauth2.admin.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - Oauth2 客户端创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Oauth2ClientCreateReqVO extends Oauth2ClientBaseVO {

}
