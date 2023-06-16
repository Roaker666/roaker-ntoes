package com.roaker.notes.infra.encrypt.vo;

import com.roaker.notes.commons.db.core.dataobject.PageParam;
import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author lei.rao
 * @since 1.0
 */
@Schema(description = "管理后台 - 动态参数分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicPageReqVO extends PageParam {
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
