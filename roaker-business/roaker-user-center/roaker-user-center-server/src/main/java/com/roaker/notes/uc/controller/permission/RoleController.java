package com.roaker.notes.uc.controller.permission;

import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.excel.utils.ExcelUtils;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.converter.permission.PermissionConverter;
import com.roaker.notes.uc.dal.dataobject.permission.RoleInfoDO;
import com.roaker.notes.uc.service.permission.RoleService;
import com.roaker.notes.uc.vo.permission.*;
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
import java.util.Comparator;
import java.util.List;

import static com.roaker.notes.vo.CommonResult.success;
import static java.util.Collections.singleton;

@Tag(name = "管理后台 - 角色")
@RestController
@RequestMapping("/system/role")
@Validated
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "创建角色")
    public CommonResult<String> createRole(@Valid @RequestBody RoleCreateReqVO reqVO) {
        return success(roleService.createRole(reqVO, null));
    }

    @PutMapping("/update")
    @Operation(summary = "修改角色")
    public CommonResult<Boolean> updateRole(@Valid @RequestBody RoleUpdateReqVO reqVO) {
        roleService.updateRole(reqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "修改角色状态")
    public CommonResult<Boolean> updateRoleStatus(@Valid @RequestBody RoleUpdateStatusReqVO reqVO) {
        roleService.updateRoleStatus(reqVO.getRoleId(), reqVO.getStatus());
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色")
    @Parameter(name = "id", description = "角色编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteRole(@RequestParam("roleId") String roleId) {
        roleService.deleteRole(roleId);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得角色信息")
    public CommonResult<RoleRespVO> getRole(@RequestParam("roleId") String roleId) {
        RoleInfoDO role = roleService.getRole(roleId);
        return success(PermissionConverter.INSTANCE.convert(role));
    }

    @GetMapping("/page")
    @Operation(summary = "获得角色分页")
    public CommonResult<PageResult<RoleInfoDO>> getRolePage(RolePageReqVO reqVO) {
        return success(roleService.getRolePage(reqVO));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取角色精简信息列表", description = "只包含被开启的角色，主要用于前端的下拉选项")
    public CommonResult<List<RoleSimpleRespVO>> getSimpleRoleList() {
        // 获得角色列表，只要开启状态的
        List<RoleInfoDO> list = roleService.getRoleListByStatus(singleton(CommonStatusEnum.ENABLE.getCode()));
        // 排序后，返回给前端
        list.sort(Comparator.comparing(RoleInfoDO::getSort));
        return success(PermissionConverter.INSTANCE.convertList02(list));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, @Validated RoleExportReqVO reqVO) throws IOException {
        List<RoleInfoDO> list = roleService.getRoleList(reqVO);
        List<RoleExcelVO> data = PermissionConverter.INSTANCE.convertList03(list);
        // 输出
        ExcelUtils.write(response, "角色数据.xls", "角色列表", RoleExcelVO.class, data);
    }

}
