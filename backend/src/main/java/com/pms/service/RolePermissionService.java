package com.pms.service;

import com.pms.entity.Menu;
import com.pms.entity.RoleMenu;
import com.pms.repository.MenuRepository;
import com.pms.repository.RoleMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {

    private final RoleMenuRepository roleMenuRepository;
    private final MenuRepository menuRepository;

    public RolePermissionService(RoleMenuRepository roleMenuRepository, MenuRepository menuRepository) {
        this.roleMenuRepository = roleMenuRepository;
        this.menuRepository = menuRepository;
    }

    public List<Menu> getMenusByRoleId(String roleId) {
        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
        List<String> resourceIds = roleMenus.stream()
                .map(RoleMenu::getResourceId)
                .collect(Collectors.toList());
        
        // 查询完整的 Menu 对象
        return menuRepository.findAllById(resourceIds);
    }

    @Transactional
    public void saveMenus(String roleId, List<String> menuIds) {
        // 先删除旧的 RoleMenu 关联
        roleMenuRepository.deleteByRoleId(roleId);

        // 批量插入新的关联
        if (menuIds != null && !menuIds.isEmpty()) {
            for (String menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setResourceId(menuId);
                roleMenuRepository.save(roleMenu);
            }
        }
    }
}
