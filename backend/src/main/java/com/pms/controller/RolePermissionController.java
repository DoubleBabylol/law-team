package com.pms.controller;

import com.pms.dto.ApiResponse;
import com.pms.entity.Menu;
import com.pms.service.RolePermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permission")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping("/menus/{roleId}")
    public ApiResponse<List<Menu>> getMenusByRoleId(@PathVariable String roleId) {
        return ApiResponse.success(rolePermissionService.getMenusByRoleId(roleId));
    }

    @PostMapping("/save-menus/{roleId}")
    public ApiResponse<Void> saveMenus(@PathVariable String roleId, 
                                       @RequestBody List<String> menuIds) {
        rolePermissionService.saveMenus(roleId, menuIds);
        return ApiResponse.success(null);
    }
}
