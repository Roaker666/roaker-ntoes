package com.roaker.notes.uc.controller.encrypt;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.excel.utils.ExcelUtils;
import com.roaker.notes.uc.converter.encrypt.DynamicConverter;
import com.roaker.notes.uc.dal.dataobject.encrypt.DynamicDictDO;
import com.roaker.notes.uc.dto.encrypt.DynamicDictDTO;
import com.roaker.notes.uc.service.encrypt.DynamicDictService;
import com.roaker.notes.uc.vo.encrypt.*;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "管理后台 - 动态参数")
@RestController
@RequestMapping("/infra/dynamic-dict")
@Validated
public class DynamicDictController {

    @Resource
    private DynamicDictService dynamicDictService;

    @PostMapping("/create")
    @Operation(summary = "创建动态参数")
    public CommonResult<Long> createDynamicDict(@Valid @RequestBody DynamicCreateReqVO createReqVO) {
        return success(dynamicDictService.createDynamicDict(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态参数")
    public CommonResult<Boolean> updateDynamicDict(@Valid @RequestBody DynamicUpdateReqVO updateReqVO) {
        dynamicDictService.updateDynamicDict(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态参数")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDynamicDict(@RequestParam("id") Long id) {
        dynamicDictService.deleteDynamicDict(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态参数")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<DynamicDictDTO> getDynamicDict(@RequestParam("id") Long id) {
        DynamicDictDO dynamicDict = dynamicDictService.getDynamicDict(id);
        return success(DynamicConverter.INSTANCE.convert(dynamicDict));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态参数分页")
    public CommonResult<PageResult<DynamicDictDTO>> getDynamicDictPage(@Valid DynamicPageReqVO pageVO) {
        PageResult<DynamicDictDO> pageResult = dynamicDictService.getDynamicDictPage(pageVO);
        return success(DynamicConverter.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list")
    @Operation(summary = "获得动态参数列表")
    public CommonResult<List<DynamicDictDTO>> getDynamicDictList(@Valid LocalDateTime minUpdateTime) {
        List<DynamicDictDTO> dynamicDictList = dynamicDictService.getDynamicDictList(minUpdateTime);
        return success(dynamicDictList);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出动态参数 Excel")
    public void exportDynamicDictExcel(@Valid DynamicExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<DynamicDictDO> list = dynamicDictService.getDynamicDictList(exportReqVO);
        // 导出 Excel
        List<DynamicExportRespVO> datas = DynamicConverter.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "动态参数.xls", "数据", DynamicExportRespVO.class, datas);
    }

}
