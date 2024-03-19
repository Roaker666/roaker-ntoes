package com.roaker.notes.uc.vo.dict.data;

import com.roaker.notes.uc.vo.dict.DynamicBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lei.rao
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "管理后台 - 动态参数更新请求")
public class DynamicUpdateReqVO extends DynamicBaseVO {
    private Long id;
}
