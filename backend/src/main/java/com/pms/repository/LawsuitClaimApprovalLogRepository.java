package com.pms.repository;

import com.pms.entity.LawsuitClaimApprovalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LawsuitClaimApprovalLogRepository extends JpaRepository<LawsuitClaimApprovalLog, String> {

    List<LawsuitClaimApprovalLog> findByLawsuitClaimIdOrderByCreatedAtDesc(String lawsuitClaimId);
}
