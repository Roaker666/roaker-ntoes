package com.roaker.notes.infra.encrypt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@Schema(description = "管理后台 - 文件配置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileConfigUpdateReqVO extends FileConfigBaseVO {

    @Schema(description = "编号", required = true, example = "1")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "存储配置,配置是动态参数，所以使用 Map 接收", required = true)
    @NotNull(message = "存储配置不能为空")
    private Map<String, Object> config;

}
