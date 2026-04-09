package com.pms.repository;

import com.pms.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {

    List<Branch> findByLevel(Integer level);

    List<Branch> findByPrioBranchNo(String prioBranchNo);
}
