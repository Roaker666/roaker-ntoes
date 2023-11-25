package com.roaker.notes.infra.encrypt.vo;

import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@Schema(description = "管理后台 - 动态参数导出请求")
public class DynamicExportReqVO implements Serializable {
    /**
     * 配置类型;{@link DynamicDictTypeEnums}
     */
    @Schema(name = "配置类型", description = "1:模板枚举配置 2:下拉筛选配置 3:国家编码配置")
    private Integer type;
    /**
     * 配置所属类型key
     */
    @Schema(name = "配置所属类型key", description = "")
    private String bizKey;
    /**
     * 业务码
     */
    @Schema(name = "业务码", description = "")
    private Integer code;
    /**
     * 上游业务码
     */
    @Schema(name = "上游业务码", description = "")
    private String originCode;
}
