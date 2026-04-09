package com.pms.repository;

import com.pms.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

    List<Menu> findByParentResourceId(String parentResourceId);

    List<Menu> findByResourceType(String resourceType);
}
