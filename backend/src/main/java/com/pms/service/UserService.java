package com.pms.service;

import com.pms.dto.PageResult;
import com.pms.dto.RoleDTO;
import com.pms.dto.UserAddRequest;
import com.pms.dto.UserUpdateRequest;
import com.pms.dto.UserVO;
import com.pms.entity.Role;
import com.pms.entity.User;
import com.pms.entity.UserRole;
import com.pms.exception.BusinessException;
import com.pms.repository.RoleRepository;
import com.pms.repository.UserRepository;
import com.pms.repository.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PASSWORD = "123456";

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PageResult<UserVO> list(int page, int size, String keyword) {
        // 前端传的是 1-based，JPA Pageable 是 0-based
        Pageable pageable = PageRequest.of(page - 1, size);
        
        Page<User> userPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.searchByKeyword(keyword.trim(), pageable);
        }

        List<UserVO> records = userPage.getContent().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(
                records,
                userPage.getTotalElements(),
                page,
                size
        );
    }

    public UserVO getById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        UserVO vo = convertToVO(user);
        
        // 获取用户角色
        List<RoleDTO> roles = getUserRoles(id);
        vo.setRoles(roles);
        
        return vo;
    }

    @Transactional
    public UserVO add(UserAddRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        // 检查工号是否已存在
        if (userRepository.existsByClerkNo(request.getClerkNo())) {
            throw new BusinessException("工号已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setClerkNo(request.getClerkNo());
        user.setBranchNo(request.getBranchNo());
        user.setBranchName(request.getBranchName());
        user.setDeptName(request.getDeptName());
        user.setDeptNo(request.getDeptNo());
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setStatus(request.getStatus());

        User savedUser = userRepository.save(user);

        // 保存用户角色关联
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            saveUserRoles(savedUser.getUserId(), request.getRoleIds());
        }

        return convertToVO(savedUser);
    }

    @Transactional
    public UserVO update(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        // 如果用户名改变了，检查新名称是否已存在
        if (!user.getUsername().equals(request.getUsername()) 
                && userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        user.setUsername(request.getUsername());
        user.setBranchNo(request.getBranchNo());
        user.setBranchName(request.getBranchName());
        user.setDeptName(request.getDeptName());
        user.setDeptNo(request.getDeptNo());
        user.setStatus(request.getStatus());

        User updatedUser = userRepository.save(user);

        // 删除旧的角色关联，插入新的关联
        userRoleRepository.deleteByUserId(id);
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            saveUserRoles(id, request.getRoleIds());
        }

        UserVO vo = convertToVO(updatedUser);
        List<RoleDTO> roles = getUserRoles(id);
        vo.setRoles(roles);
        return vo;
    }

    @Transactional
    public void delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        // 删除用户角色关联
        userRoleRepository.deleteByUserId(id);

        // 删除用户
        userRepository.delete(user);
    }

    @Transactional
    public void resetPassword(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userRepository.save(user);
    }

    private void saveUserRoles(String userId, List<String> roleIds) {
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleRepository.save(userRole);
        }
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

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setClerkNo(user.getClerkNo());
        vo.setBranchNo(user.getBranchNo());
        vo.setBranchName(user.getBranchName());
        vo.setDeptName(user.getDeptName());
        vo.setDeptNo(user.getDeptNo());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());
        return vo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        
        // 获取用户角色
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getUserId());
        List<SimpleGrantedAuthority> authorities = userRoles.stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRoleId()))
                .collect(Collectors.toList());
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus(),
                true,
                true,
                true,
                authorities
        );
    }
}
