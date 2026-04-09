package com.pms.repository;

import com.pms.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(String userId);

    List<UserRole> findByRoleId(String roleId);

    void deleteByUserId(String userId);

    void deleteByRoleId(String roleId);

    boolean existsByRoleId(String roleId);
}
