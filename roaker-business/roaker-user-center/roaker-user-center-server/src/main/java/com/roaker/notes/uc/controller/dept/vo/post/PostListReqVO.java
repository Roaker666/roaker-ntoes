package com.roaker.notes.uc.controller.dept.vo.post;

import com.roaker.notes.enums.CommonStatusEnum;
import io.lettuce.core.protocol.Command;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 岗位列表 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostListReqVO extends PostBaseVO {

    @Schema(description = "岗位名称，模糊匹配", example = "Roaker")
    private String name;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private CommonStatusEnum status;

}
