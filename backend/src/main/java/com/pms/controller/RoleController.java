package com.pms.controller;

import com.pms.dto.ApiResponse;
import com.pms.dto.RoleAddRequest;
import com.pms.dto.RoleDTO;
import com.pms.dto.RoleUpdateRequest;
import com.pms.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public ApiResponse<List<RoleDTO>> list() {
        return ApiResponse.success(roleService.list());
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<RoleDTO> getById(@PathVariable String id) {
        return ApiResponse.success(roleService.getById(id));
    }

    @PostMapping("/add")
    public ApiResponse<RoleDTO> add(@Valid @RequestBody RoleAddRequest request) {
        return ApiResponse.success(roleService.add(request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<RoleDTO> update(@PathVariable String id, 
                                       @Valid @RequestBody RoleUpdateRequest request) {
        return ApiResponse.success(roleService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        roleService.delete(id);
        return ApiResponse.success(null);
    }
}
