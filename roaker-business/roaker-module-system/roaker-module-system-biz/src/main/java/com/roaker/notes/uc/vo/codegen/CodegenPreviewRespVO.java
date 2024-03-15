package com.roaker.notes.uc.vo.codegen;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - 代码生成预览 Response VO，注意，每个文件都是一个该对象")
@Data
@Accessors(chain = true)
public class CodegenPreviewRespVO {

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "java/cn/iocoder/Roaker/adminserver/modules/system/controller/test/SysTestDemoController.java")
    private String filePath;

    @Schema(description = "代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello World")
    private String code;

}
