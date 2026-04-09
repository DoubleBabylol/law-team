package com.pms.repository;

import com.pms.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

    List<RoleMenu> findByRoleId(String roleId);

    void deleteByRoleId(String roleId);

    void deleteByResourceId(String resourceId);
}
