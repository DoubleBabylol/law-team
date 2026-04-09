package com.pms.controller;

import com.pms.dto.ApiResponse;
import com.pms.dto.PageResult;
import com.pms.dto.UserAddRequest;
import com.pms.dto.UserUpdateRequest;
import com.pms.dto.UserVO;
import com.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ApiResponse<PageResult<UserVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(userService.list(page, size, keyword));
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<UserVO> getById(@PathVariable String id) {
        return ApiResponse.success(userService.getById(id));
    }

    @PostMapping("/add")
    public ApiResponse<UserVO> add(@Valid @RequestBody UserAddRequest request) {
        return ApiResponse.success(userService.add(request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<UserVO> update(@PathVariable String id, 
                                      @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ApiResponse.success(null);
    }

    @PutMapping("/reset-password/{id}")
    public ApiResponse<Void> resetPassword(@PathVariable String id) {
        userService.resetPassword(id);
        return ApiResponse.success(null);
    }
}
