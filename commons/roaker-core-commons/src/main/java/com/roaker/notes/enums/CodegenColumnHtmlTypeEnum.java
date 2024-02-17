package com.roaker.notes.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lei.rao
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum CodegenColumnHtmlTypeEnum implements CommonEnum {
    INPUT(1, "input"), // 文本框
    TEXTAREA(2, "textarea"), // 文本域
    SELECT(3, "select"), // 下拉框
    RADIO(4, "radio"), // 单选框
    CHECKBOX(5, "checkbox"), // 复选框
    DATETIME(6, "datetime"), // 日期控件
    UPLOAD_IMAGE(7, "upload_image"), // 上传图片
    UPLOAD_FILE(8, "upload_file"), // 上传文件
    EDITOR(9, "editor"), // 富文本控件
    ;
    @Getter
    @EnumValue
    private final Integer code;
    private final String name;
}
