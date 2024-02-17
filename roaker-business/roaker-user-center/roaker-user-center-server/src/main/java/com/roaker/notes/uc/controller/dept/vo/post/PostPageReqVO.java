package com.roaker.notes.uc.controller.dept.vo.post;

import com.roaker.notes.commons.db.PageParam;
import com.roaker.notes.enums.CommonStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 岗位分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostPageReqVO extends PageParam {

    @Schema(description = "岗位编码，模糊匹配", example = "roaker")
    private String code;

    @Schema(description = "岗位名称，模糊匹配", example = "Roaker")
    private String name;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private CommonStatusEnum status;

}
