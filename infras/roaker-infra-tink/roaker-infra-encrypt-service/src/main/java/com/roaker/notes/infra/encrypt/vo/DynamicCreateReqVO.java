package com.roaker.notes.infra.encrypt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lei.rao
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "管理后台 - 动态参数创建请求")
public class DynamicCreateReqVO extends DynamicBaseVO{

}
