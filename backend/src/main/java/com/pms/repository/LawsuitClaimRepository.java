package com.pms.repository;

import com.pms.entity.ApprovalStatus;
import com.pms.entity.LawsuitClaim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LawsuitClaimRepository extends JpaRepository<LawsuitClaim, String> {

    @Query("SELECT lc.taskCode FROM LawsuitClaim lc WHERE lc.taskCode LIKE :prefix ORDER BY lc.taskCode DESC")
    Optional<String> findMaxTaskCodeByPrefix(@Param("prefix") String prefix);

    Page<LawsuitClaim> findByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);

    @Query("SELECT lc FROM LawsuitClaim lc WHERE lc.taskName LIKE %:keyword% OR lc.taskCode LIKE %:keyword%")
    Page<LawsuitClaim> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT lc FROM LawsuitClaim lc WHERE lc.approvalStatus = :status AND (lc.taskName LIKE %:keyword% OR lc.taskCode LIKE %:keyword%)")
    Page<LawsuitClaim> searchByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") ApprovalStatus status, Pageable pageable);
}
