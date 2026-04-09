package com.pms.controller;

import com.pms.dto.ApiResponse;
import com.pms.dto.MenuAddRequest;
import com.pms.dto.MenuTreeDTO;
import com.pms.dto.MenuUpdateRequest;
import com.pms.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/tree")
    public ApiResponse<List<MenuTreeDTO>> tree() {
        return ApiResponse.success(menuService.tree());
    }

    @GetMapping("/list")
    public ApiResponse<List<MenuTreeDTO>> list() {
        return ApiResponse.success(menuService.list());
    }

    @PostMapping("/add")
    public ApiResponse<MenuTreeDTO> add(@Valid @RequestBody MenuAddRequest request) {
        return ApiResponse.success(menuService.add(request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<MenuTreeDTO> update(@PathVariable String id, 
                                           @Valid @RequestBody MenuUpdateRequest request) {
        return ApiResponse.success(menuService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        menuService.delete(id);
        return ApiResponse.success(null);
    }
}
