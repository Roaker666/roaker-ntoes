package com.roaker.notes.dynamic.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "动态字典表", description = "")
public class DynamicDictDO {

    /**
     * 配置类型;1:模板枚举配置 2:下拉筛选配置 3:国家编码配置
     */
    @ApiModelProperty(name = "配置类型", notes = "1:模板枚举配置 2:下拉筛选配置 3:国家编码配置")
    private Integer type;
    /**
     * 配置业务名称
     */
    @ApiModelProperty(name = "配置业务名称", notes = "")
    private String bizName;
    /**
     * 配置所属类型key
     */
    @ApiModelProperty(name = "配置所属类型key", notes = "")
    private String bizKey;
    /**
     * 模板枚举配置全类目
     */
    @ApiModelProperty(name = "模板枚举配置全类目", notes = "")
    private String bizClass;
    /**
     * 业务码
     */
    @ApiModelProperty(name = "业务码", notes = "")
    private Integer code;
    /**
     * 业务描述名称
     */
    @ApiModelProperty(name = "业务描述名称", notes = "")
    private String name;
    /**
     * 上游业务码
     */
    @ApiModelProperty(name = "上游业务码", notes = "")
    private String originCode;
    /**
     * 上游业务描述名称
     */
    @ApiModelProperty(name = "上游业务描述名称", notes = "")
    private String originName;

}
