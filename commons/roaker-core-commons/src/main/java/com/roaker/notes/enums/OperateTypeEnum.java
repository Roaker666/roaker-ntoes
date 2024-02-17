package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperateTypeEnum implements CommonEnum {

    GET(1, "GET"),
    /**
     * 新增
     */
    CREATE(2, "CREATE"),
    /**
     * 修改
     */
    UPDATE(3, "UPDATE"),
    /**
     * 删除
     */
    DELETE(4, "DELETE"),
    /**
     * 导出
     */
    EXPORT(5, "EXPORT"),
    /**
     * 导入
     */
    IMPORT(6, "IMPORT"),
    /**
     * 其它
     * <p>
     * 在无法归类时，可以选择使用其它。因为还有操作名可以进一步标识
     */
    OTHER(0, "OTHER");

    /**
     * 类型
     */
    @EnumValue
    private final Integer code;
    private final String name;

}
