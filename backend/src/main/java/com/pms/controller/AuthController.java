package com.pms.controller;

import com.pms.dto.*;
import com.pms.entity.Menu;
import com.pms.entity.Role;
import com.pms.entity.RoleMenu;
import com.pms.entity.User;
import com.pms.entity.UserRole;
import com.pms.repository.MenuRepository;
import com.pms.repository.RoleMenuRepository;
import com.pms.repository.RoleRepository;
import com.pms.repository.UserRepository;
import com.pms.repository.UserRoleRepository;
import com.pms.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RoleMenuRepository roleMenuRepository;
    private final MenuRepository menuRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          UserRoleRepository userRoleRepository,
                          RoleRepository roleRepository,
                          RoleMenuRepository roleMenuRepository,
                          MenuRepository menuRepository,
                          JwtUtils jwtUtils,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.roleMenuRepository = roleMenuRepository;
        this.menuRepository = menuRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 查找用户
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error(401, "用户名不存在"));
        }

        User user = userOptional.get();

        // 校验密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(ApiResponse.error(401, "密码错误"));
        }

        // 校验用户状态
        if (user.getStatus() == null || !user.getStatus()) {
            return ResponseEntity.ok(ApiResponse.error(403, "该账号已被禁用，请联系管理员"));
        }

        // 生成 JWT Token
        String token = jwtUtils.generateToken(user.getUserId(), user.getUsername());

        // 查询用户角色
        List<RoleDTO> roles = getUserRoles(user.getUserId());

        // 查询用户菜单（树形结构）
        List<MenuTreeDTO> menus = getUserMenus(user.getUserId());

        // 组装响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setRoles(roles);
        response.setMenus(menus);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        // JWT 无状态，前端清除 token 即可
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private List<RoleDTO> getUserRoles(String userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        List<Role> roles = roleRepository.findAllById(roleIds);
        return roles.stream()
                .map(role -> new RoleDTO(
                        role.getRoleId(),
                        role.getRoleName(),
                        role.getRoleGroup(),
                        role.getStatus()
                ))
                .collect(Collectors.toList());
    }

    private List<MenuTreeDTO> getUserMenus(String userId) {
        // 获取用户角色ID列表
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        // 获取角色关联的菜单ID列表
        Set<String> resourceIds = new HashSet<>();
        for (String roleId : roleIds) {
            List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
            resourceIds.addAll(roleMenus.stream()
                    .map(RoleMenu::getResourceId)
                    .collect(Collectors.toList()));
        }

        if (resourceIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 查询菜单详情
        List<Menu> menus = menuRepository.findAllById(resourceIds);
        
        // 过滤出启用的菜单
        menus = menus.stream()
                .filter(menu -> menu.getStatus() != null && menu.getStatus())
                .sorted(Comparator.comparing(Menu::getSortOrder, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());

        // 构建树形结构
        return buildMenuTree(menus);
    }

    private List<MenuTreeDTO> buildMenuTree(List<Menu> menus) {
        Map<String, MenuTreeDTO> menuMap = new HashMap<>();
        List<MenuTreeDTO> rootMenus = new ArrayList<>();

        // 首先将所有菜单转换为 DTO 并存入 map
        for (Menu menu : menus) {
            MenuTreeDTO dto = convertToMenuTreeDTO(menu);
            menuMap.put(dto.getResourceId(), dto);
        }

        // 构建树形结构
        for (MenuTreeDTO dto : menuMap.values()) {
            if (dto.getParentResourceId() == null || dto.getParentResourceId().isEmpty()) {
                // 根菜单
                rootMenus.add(dto);
            } else {
                // 子菜单
                MenuTreeDTO parent = menuMap.get(dto.getParentResourceId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                } else {
                    // 父菜单不在当前用户的权限范围内，作为根菜单处理
                    rootMenus.add(dto);
                }
            }
        }

        // 按 sortOrder 排序
        rootMenus.sort(Comparator.comparing(MenuTreeDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        
        return rootMenus;
    }

    private MenuTreeDTO convertToMenuTreeDTO(Menu menu) {
        MenuTreeDTO dto = new MenuTreeDTO();
        dto.setResourceId(menu.getResourceId());
        dto.setParentResourceId(menu.getParentResourceId());
        dto.setResourceName(menu.getResourceName());
        dto.setResourceType(menu.getResourceType());
        dto.setResourcePath(menu.getResourcePath());
        dto.setPermission(menu.getPermission());
        dto.setIcon(menu.getIcon());
        dto.setSortOrder(menu.getSortOrder());
        dto.setStatus(menu.getStatus());
        return dto;
    }
}
