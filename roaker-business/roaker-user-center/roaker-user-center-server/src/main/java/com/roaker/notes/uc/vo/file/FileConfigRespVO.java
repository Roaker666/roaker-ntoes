package com.roaker.notes.uc.vo.file;

import com.roaker.notes.file.core.client.FileClientConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 文件配置 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileConfigRespVO extends FileConfigBaseVO {

    @Schema(description = "编号", required = true, example = "1")
    private Long id;

    @Schema(description = "存储器,参见 FileStorageEnum 枚举类", required = true, example = "1")
    @NotNull(message = "存储器不能为空")
    private Integer storage;

    @Schema(description = "是否为主配置", required = true, example = "true")
    @NotNull(message = "是否为主配置不能为空")
    private Boolean master;

    @Schema(description = "存储配置", required = true)
    private FileClientConfig config;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
