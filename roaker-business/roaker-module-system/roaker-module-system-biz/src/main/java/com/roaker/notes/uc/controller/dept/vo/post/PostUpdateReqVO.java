package com.roaker.notes.uc.controller.dept.vo.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 岗位更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostUpdateReqVO extends PostBaseVO {

    @Schema(description = "岗位编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "岗位编号不能为空")
    private Long id;

}
