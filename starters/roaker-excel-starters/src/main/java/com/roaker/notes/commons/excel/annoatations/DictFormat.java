package com.roaker.notes.commons.excel.annoatations;

import com.roaker.notes.dynamic.enums.CommonEnum;

import java.lang.annotation.*;

/**
 * 字典格式化
 * 实现将字典数据的值，格式化成字典数据的标签
 * @author lei.rao
 * @since 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DictFormat {
    /**
     * @return 字典类型
     */
    Class<? extends CommonEnum> value();
}
