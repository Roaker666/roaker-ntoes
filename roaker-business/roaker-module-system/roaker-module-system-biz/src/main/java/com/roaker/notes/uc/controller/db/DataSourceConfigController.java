package com.roaker.notes.uc.controller.db;

import com.roaker.notes.uc.converter.db.DataSourceConfigConvert;
import com.roaker.notes.uc.dal.dataobject.db.DataSourceConfigDO;
import com.roaker.notes.uc.service.db.DataSourceConfigService;
import com.roaker.notes.uc.vo.db.DataSourceConfigCreateReqVO;
import com.roaker.notes.uc.vo.db.DataSourceConfigRespVO;
import com.roaker.notes.uc.vo.db.DataSourceConfigUpdateReqVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

import static com.roaker.notes.vo.CommonResult.success;


@Tag(name = "管理后台 - 数据源配置")
@RestController
@RequestMapping("/infra/data-source-config")
@Validated
public class DataSourceConfigController {

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建数据源配置")
    @PreAuthorize("@ss.hasPermission('sys:data-source-config:create')")
    public CommonResult<Long> createDataSourceConfig(@Valid @RequestBody DataSourceConfigCreateReqVO createReqVO) {
        return success(dataSourceConfigService.createDataSourceConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新数据源配置")
    @PreAuthorize("@ss.hasPermission('sys:data-source-config:update')")
    public CommonResult<Boolean> updateDataSourceConfig(@Valid @RequestBody DataSourceConfigUpdateReqVO updateReqVO) {
        dataSourceConfigService.updateDataSourceConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除数据源配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('sys:data-source-config:delete')")
    public CommonResult<Boolean> deleteDataSourceConfig(@RequestParam("id") Long id) {
        dataSourceConfigService.deleteDataSourceConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得数据源配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('sys:data-source-config:query')")
    public CommonResult<DataSourceConfigRespVO> getDataSourceConfig(@RequestParam("id") Long id) {
        DataSourceConfigDO dataSourceConfig = dataSourceConfigService.getDataSourceConfig(id);
        return success(DataSourceConfigConvert.INSTANCE.convert(dataSourceConfig));
    }

    @GetMapping("/list")
    @Operation(summary = "获得数据源配置列表")
    @PreAuthorize("@ss.hasPermission('sys:data-source-config:query')")
    public CommonResult<List<DataSourceConfigRespVO>> getDataSourceConfigList() {
        List<DataSourceConfigDO> list = dataSourceConfigService.getDataSourceConfigList();
        return success(DataSourceConfigConvert.INSTANCE.convertList(list));
    }

}
