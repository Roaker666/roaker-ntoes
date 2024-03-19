package com.roaker.notes.uc.vo.dict.data;

import com.alibaba.excel.annotation.ExcelProperty;
import com.roaker.notes.commons.excel.annoatations.DictFormat;
import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class DynamicExportRespVO implements Serializable {
    /**
     * 配置类型;{@link DynamicDictTypeEnums}
     */
    @ExcelProperty(value = "配置类型")
    @DictFormat(DynamicDictTypeEnums.class)
    private DynamicDictTypeEnums type;
    /**
     * 配置业务名称
     */
    @ExcelProperty(value = "配置业务名称")
    private String bizName;
    /**
     * 配置所属类型key
     */
    @ExcelProperty(value = "配置所属类型key")
    private String bizKey;
    /**
     * 模板枚举配置全类目
     */
    @ExcelProperty(value = "模板枚举配置全类目")
    private String bizClass;
    /**
     * 业务码
     */
    @ExcelProperty(value = "业务码")
    private Integer code;
    /**
     * 业务描述名称
     */
    @ExcelProperty(value = "业务描述名称")
    private String name;
    /**
     * 上游业务码
     */
    @ExcelProperty(value = "上游业务码")
    private String originCode;
    /**
     * 上游业务描述名称
     */
    @ExcelProperty(value = "上游业务描述名称")
    private String originName;
}
