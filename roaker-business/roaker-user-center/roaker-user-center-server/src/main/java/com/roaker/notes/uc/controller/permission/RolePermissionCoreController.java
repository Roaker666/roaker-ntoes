package com.roaker.notes.uc.controller.permission;

import com.roaker.notes.uc.service.permission.RolePermissionCoreService;
import com.roaker.notes.uc.vo.permission.PermissionAssignRolePermissionInfoReqVO;
import com.roaker.notes.uc.vo.permission.PermissionAssignUserRoleReqVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.roaker.notes.vo.CommonResult.success;

/**
 * 权限 Controller，提供赋予用户、角色的权限的 API 接口
 *
 * @author lei.rao
 */
@Tag(name = "管理后台 - 权限")
@RestController
@RequestMapping("/system/permission")
public class RolePermissionCoreController {

    @Resource
    private RolePermissionCoreService rolePermissionCoreService;

    @Operation(summary = "获得角色拥有的菜单编号")
    @Parameter(name = "roleId", description = "角色编号", required = true)
    @GetMapping("/list-role-resources")
    public CommonResult<Set<String>> listRolePermissions(String roleId) {
        return success(rolePermissionCoreService.getRolePermissionInfoIds(roleId));
    }

    @PostMapping("/assign-role-menu")
    @Operation(summary = "赋予角色菜单")
    public CommonResult<Boolean> assignRolePermission(@Validated @RequestBody PermissionAssignRolePermissionInfoReqVO reqVO) {
        // 执行菜单的分配
        rolePermissionCoreService.assignRolePermissionInfo(reqVO.getRoleId(), reqVO.getPermissionInfoIds());
        return success(true);
    }


    @Operation(summary = "获得管理员拥有的角色编号列表")
    @Parameter(name = "userId", description = "用户编号", required = true)
    @GetMapping("/list-user-roles")
    public CommonResult<Set<String>> listAdminRoles(@RequestParam("userId") String userId) {
        return success(rolePermissionCoreService.getUserRoleIdListByUserId(userId, null));
    }

    @Operation(summary = "赋予用户角色")
    @PostMapping("/assign-user-role")
    public CommonResult<Boolean> assignUserRole(@Validated @RequestBody PermissionAssignUserRoleReqVO reqVO) {
        rolePermissionCoreService.assignUserRole(reqVO.getUserId(), null, reqVO.getRoleIds());
        return success(true);
    }

}
