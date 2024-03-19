package com.roaker.notes.uc.dto.encrypt;

import com.roaker.notes.dynamic.enums.DynamicDictTypeEnums;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class DynamicDictDTO implements Serializable {
    /**
     * 配置类型;{@link DynamicDictTypeEnums}
     */
    private DynamicDictTypeEnums type;
    /**
     * 配置业务名称
     */
    private String bizName;
    /**
     * 配置所属类型key
     */
    private String bizKey;
    /**
     * 模板枚举配置全类目
     */
    private String bizClass;
    /**
     * 业务码
     */
    private Integer code;
    /**
     * 业务描述名称
     */
    private String name;
    /**
     * 标签
     */
    private String label;
    /**
     * 标签值
     */
    private String value;
    private LocalDateTime updateTime;
}
