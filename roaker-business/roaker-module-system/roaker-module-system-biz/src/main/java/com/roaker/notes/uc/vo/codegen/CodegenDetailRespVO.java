package com.roaker.notes.uc.vo.codegen;

import com.roaker.notes.uc.vo.codegen.column.CodegenColumnRespVO;
import com.roaker.notes.uc.vo.codegen.table.CodegenTableRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 代码生成表和字段的明细 Response VO")
@Data
public class CodegenDetailRespVO {

    @Schema(description = "表定义")
    private CodegenTableRespVO table;

    @Schema(description = "字段定义")
    private List<CodegenColumnRespVO> columns;

}
