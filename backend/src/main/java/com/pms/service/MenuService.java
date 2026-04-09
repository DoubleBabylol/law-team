package com.pms.service;

import com.pms.dto.MenuAddRequest;
import com.pms.dto.MenuTreeDTO;
import com.pms.dto.MenuUpdateRequest;
import com.pms.entity.Menu;
import com.pms.exception.BusinessException;
import com.pms.repository.MenuRepository;
import com.pms.repository.RoleMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RoleMenuRepository roleMenuRepository;

    public MenuService(MenuRepository menuRepository,
                       RoleMenuRepository roleMenuRepository) {
        this.menuRepository = menuRepository;
        this.roleMenuRepository = roleMenuRepository;
    }

    public List<MenuTreeDTO> tree() {
        List<Menu> menus = menuRepository.findAll();
        return buildMenuTree(menus);
    }

    public List<MenuTreeDTO> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .sorted(Comparator.comparing(Menu::getSortOrder, Comparator.nullsLast(Integer::compareTo)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuTreeDTO add(MenuAddRequest request) {
        Menu menu = new Menu();
        menu.setParentResourceId(request.getParentResourceId());
        menu.setResourceName(request.getResourceName());
        menu.setResourceType(request.getResourceType());
        menu.setResourcePath(request.getResourcePath());
        menu.setPermission(request.getPermission());
        menu.setIcon(request.getIcon());
        menu.setSortOrder(request.getSortOrder());
        menu.setStatus(request.getStatus());

        Menu savedMenu = menuRepository.save(menu);
        return convertToDTO(savedMenu);
    }

    @Transactional
    public MenuTreeDTO update(String id, MenuUpdateRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new BusinessException("菜单不存在"));

        menu.setParentResourceId(request.getParentResourceId());
        menu.setResourceName(request.getResourceName());
        menu.setResourceType(request.getResourceType());
        menu.setResourcePath(request.getResourcePath());
        menu.setPermission(request.getPermission());
        menu.setIcon(request.getIcon());
        menu.setSortOrder(request.getSortOrder());
        menu.setStatus(request.getStatus());

        Menu updatedMenu = menuRepository.save(menu);
        return convertToDTO(updatedMenu);
    }

    @Transactional
    public void delete(String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new BusinessException("菜单不存在"));

        // 递归删除所有子节点
        deleteChildren(id);

        // 删除当前菜单的 RoleMenu 关联
        roleMenuRepository.deleteByResourceId(id);

        // 删除当前菜单
        menuRepository.delete(menu);
    }

    private void deleteChildren(String parentId) {
        List<Menu> children = menuRepository.findByParentResourceId(parentId);
        for (Menu child : children) {
            // 递归删除子节点的子节点
            deleteChildren(child.getResourceId());
            // 删除子节点的 RoleMenu 关联
            roleMenuRepository.deleteByResourceId(child.getResourceId());
            // 删除子节点
            menuRepository.delete(child);
        }
    }

    private List<MenuTreeDTO> buildMenuTree(List<Menu> menus) {
        Map<String, MenuTreeDTO> menuMap = menus.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toMap(MenuTreeDTO::getResourceId, dto -> dto));

        List<MenuTreeDTO> rootMenus = new ArrayList<>();

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
                    // 父菜单不存在，作为根菜单处理
                    rootMenus.add(dto);
                }
            }
        }

        // 按 sortOrder 排序
        sortMenuTree(rootMenus);

        return rootMenus;
    }

    private void sortMenuTree(List<MenuTreeDTO> menus) {
        menus.sort(Comparator.comparing(MenuTreeDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        for (MenuTreeDTO menu : menus) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                sortMenuTree(menu.getChildren());
            }
        }
    }

    private MenuTreeDTO convertToDTO(Menu menu) {
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
