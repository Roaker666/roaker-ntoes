package com.roaker.notes.dynamic.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "动态字典表", description = "")
public class DynamicDictDO {

    /**
     * 配置类型;1:模板枚举配置 2:下拉筛选配置 3:国家编码配置
     */
    @Schema(name = "配置类型", description = "1:模板枚举配置 2:下拉筛选配置 3:国家编码配置")
    private Integer type;
    /**
     * 配置业务名称
     */
    @Schema(name = "配置业务名称", description = "")
    private String bizName;
    /**
     * 配置所属类型key
     */
    @Schema(name = "配置所属类型key", description = "")
    private String bizKey;
    /**
     * 模板枚举配置全类目
     */
    @Schema(name = "模板枚举配置全类目", description = "")
    private String bizClass;
    /**
     * 业务码
     */
    @Schema(name = "业务码", description = "")
    private Integer code;
    /**
     * 业务描述名称
     */
    @Schema(name = "业务描述名称", description = "")
    private String name;
    /**
     * 上游业务码
     */
    @Schema(name = "上游业务码", description = "")
    private String originCode;
    /**
     * 上游业务描述名称
     */
    @Schema(name = "上游业务描述名称", description = "")
    private String originName;

}
