package com.roaker.notes.infra.encrypt.controller;

import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.infra.encrypt.convert.FileConfigConvert;
import com.roaker.notes.infra.encrypt.dal.dataobject.FileConfigDO;
import com.roaker.notes.infra.encrypt.service.FileConfigService;
import com.roaker.notes.infra.encrypt.vo.FileConfigCreateReqVO;
import com.roaker.notes.infra.encrypt.vo.FileConfigPageReqVO;
import com.roaker.notes.infra.encrypt.vo.FileConfigRespVO;
import com.roaker.notes.infra.encrypt.vo.FileConfigUpdateReqVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "基础平台 - 文件配置")
@RestController
@RequestMapping("/infra/file-config")
@Validated
@RequiredArgsConstructor
public class FileConfigController {

    private final FileConfigService fileConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建文件配置")
    public CommonResult<Long> createFileConfig(@Valid @RequestBody FileConfigCreateReqVO createReqVO) {
        return success(fileConfigService.createFileConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文件配置")
    public CommonResult<Boolean> updateFileConfig(@Valid @RequestBody FileConfigUpdateReqVO updateReqVO) {
        fileConfigService.updateFileConfig(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-master")
    @Operation(summary = "更新文件配置为 Master")
    public CommonResult<Boolean> updateFileConfigMaster(@RequestParam("id") Long id) {
        fileConfigService.updateFileConfigMaster(id);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文件配置")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteFileConfig(@RequestParam("id") Long id) {
        fileConfigService.deleteFileConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文件配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<FileConfigRespVO> getFileConfig(@RequestParam("id") Long id) {
        FileConfigDO fileConfig = fileConfigService.getFileConfig(id);
        return success(FileConfigConvert.INSTANCE.convert(fileConfig));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文件配置分页")
    public CommonResult<PageResult<FileConfigRespVO>> getFileConfigPage(@Valid FileConfigPageReqVO pageVO) {
        PageResult<FileConfigDO> pageResult = fileConfigService.getFileConfigPage(pageVO);
        return success(FileConfigConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/test")
    @Operation(summary = "测试文件配置是否正确")
    public CommonResult<String> testFileConfig(@RequestParam("id") Long id) throws Exception {
        String url = fileConfigService.testFileConfig(id);
        return success(url);
    }
}
