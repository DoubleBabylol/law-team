package com.pms.service;

import com.pms.dto.RoleAddRequest;
import com.pms.dto.RoleDTO;
import com.pms.dto.RoleUpdateRequest;
import com.pms.entity.Role;
import com.pms.exception.BusinessException;
import com.pms.repository.RoleMenuRepository;
import com.pms.repository.RoleRepository;
import com.pms.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMenuRepository roleMenuRepository;

    public RoleService(RoleRepository roleRepository,
                       UserRoleRepository userRoleRepository,
                       RoleMenuRepository roleMenuRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleMenuRepository = roleMenuRepository;
    }

    public List<RoleDTO> list() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        return convertToDTO(role);
    }

    @Transactional
    public RoleDTO add(RoleAddRequest request) {
        // 检查角色名称是否已存在
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new BusinessException("角色名称已存在");
        }

        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setRoleGroup(request.getRoleGroup());
        role.setStatus(request.getStatus());

        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Transactional
    public RoleDTO update(String id, RoleUpdateRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        // 如果角色名称改变了，检查新名称是否已存在
        if (!role.getRoleName().equals(request.getRoleName()) 
                && roleRepository.existsByRoleName(request.getRoleName())) {
            throw new BusinessException("角色名称已存在");
        }

        role.setRoleName(request.getRoleName());
        role.setRoleGroup(request.getRoleGroup());
        role.setStatus(request.getStatus());

        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Transactional
    public void delete(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        // 检查是否有用户关联
        if (userRoleRepository.existsByRoleId(id)) {
            throw new BusinessException("该角色下存在用户，无法删除");
        }

        // 删除角色菜单关联
        roleMenuRepository.deleteByRoleId(id);

        // 删除角色
        roleRepository.delete(role);
    }

    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(
                role.getRoleId(),
                role.getRoleName(),
                role.getRoleGroup(),
                role.getStatus()
        );
    }
}
