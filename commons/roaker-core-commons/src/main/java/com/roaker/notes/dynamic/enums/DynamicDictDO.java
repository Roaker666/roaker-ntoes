package com.roaker.notes.dynamic.enums;

import com.roaker.notes.enums.CommonStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
     * 配置类型;
     */
    @Schema(name = "配置类型", description = "1:模板枚举配置 2:下拉筛选配置 3:国家编码配置")
    private String type;
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
     * 标签
     */
    @Schema(name = "标签", description = "")
    private String label;
    /**
     * 标签
     */
    @Schema(name = "标签描述名称", description = "")
    private String value;
    /**
     * 状态
     * 枚举 {@link CommonStatusEnum}
     */
    @Schema(name = "状态", description = "")
    private CommonStatusEnum status;
    /**
     * 颜色类型
     * 对应到 element-ui 为 default、primary、success、info、warning、danger
     */
    @Schema(name = "颜色类型", description = "对应到 element-ui 为 default、primary、success、info、warning、danger")
    private String colorType;
    /**
     * css 样式
     */
    @Schema(name = "css 样式", description = "")
    private String cssClass;
    /**
     * 字典排序
     */
    @Schema(name = "字典排序", description = "")
    private Integer sort;
    /**
     * 备注
     */
    @Schema(name = "备注", description = "")
    private String remark;

    private LocalDateTime updateTime;

}
