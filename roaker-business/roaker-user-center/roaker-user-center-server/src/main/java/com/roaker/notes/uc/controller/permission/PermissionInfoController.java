package com.roaker.notes.uc.controller.permission;

import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.uc.converter.permission.PermissionConverter;
import com.roaker.notes.uc.dal.dataobject.permission.PermissionInfoDO;
import com.roaker.notes.uc.service.permission.PermissionInfoService;
import com.roaker.notes.vo.CommonResult;
import com.roaker.notes.uc.vo.permission.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "管理后台 - 权限")
@RestController
@RequestMapping("/system/permission")
@Validated
public class PermissionInfoController {

    @Resource
    private PermissionInfoService permissionInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建菜单")
    public CommonResult<Long> createPermissionInfo(@Valid @RequestBody PermissionInfoCreateReqVO reqVO) {
        Long permissionId = permissionInfoService.createMenu(reqVO);
        return success(permissionId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改菜单")
    public CommonResult<Boolean> updatePermissionInfo(@Valid @RequestBody PermissionInfoUpdateReqVO reqVO) {
        permissionInfoService.updateMenu(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除菜单")
    @Parameter(name = "id", description = "角色编号", required= true, example = "1024")
    public CommonResult<Boolean> deletePermissionInfo(@RequestParam("permissionId") Long permissionId) {
        permissionInfoService.deleteMenu(permissionId);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取菜单列表", description = "用于【菜单管理】界面")
    public CommonResult<List<PermissionInfoRespVO>> getPermissionInfoList(PermissionInfoListReqVO reqVO) {
        List<PermissionInfoDO> list = permissionInfoService.getMenuList(reqVO);
        list.sort(Comparator.comparing(PermissionInfoDO::getSort));
        return success(PermissionConverter.INSTANCE.convertList(list));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取菜单精简信息列表", description = "只包含被开启的菜单，用于【角色分配菜单】功能的选项。" +
            "在多租户的场景下，会只返回租户所在套餐有的菜单")
    public CommonResult<List<PermissionInfoSimpleRespVO>> getSimplePermissionInfoList() {
        // 获得菜单列表，只要开启状态的
        PermissionInfoListReqVO reqVO = new PermissionInfoListReqVO();
        reqVO.setStatus(CommonStatusEnum.ENABLE.getCode());
        List<PermissionInfoDO> list = permissionInfoService.getMenuList(reqVO);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(PermissionInfoDO::getSort));
        return success(PermissionConverter.INSTANCE.convertList04(list));
    }

    @GetMapping("/get")
    @Operation(summary = "获取菜单信息")
    public CommonResult<PermissionInfoRespVO> getPermissionInfo(Long permissionId) {
        PermissionInfoDO permission = permissionInfoService.getMenu(permissionId);
        return success(PermissionConverter.INSTANCE.convert(permission));
    }

}
