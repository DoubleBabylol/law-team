package com.pms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lawsuit_claim")
public class LawsuitClaim {

    @Id
    @Column(name = "id", length = 32, nullable = false)
    private String id;

    @Column(name = "task_name", length = 200, nullable = false)
    private String taskName;

    @Column(name = "task_code", length = 50, unique = true)
    private String taskCode;

    @Column(name = "task_type", length = 20, nullable = false)
    private String taskType;

    @Column(name = "court_document_no", length = 50)
    private String courtDocumentNo;

    @Column(name = "is_major_case", length = 10, nullable = false)
    private String isMajorCase;

    @Column(name = "is_claim_payment_dispute", length = 10, nullable = false)
    private String isClaimPaymentDispute;

    @Column(name = "is_refund_dispute", length = 10, nullable = false)
    private String isRefundDispute;

    @Column(name = "occurrence_period", length = 50, nullable = false)
    private String occurrencePeriod;

    @Column(name = "claimed_org_province", length = 50, nullable = false)
    private String claimedOrgProvince;

    @Column(name = "claimed_org_city", length = 50, nullable = false)
    private String claimedOrgCity;

    @Column(name = "claimed_org_district", length = 50, nullable = false)
    private String claimedOrgDistrict;

    @Column(name = "claimed_org_other", length = 200)
    private String claimedOrgOther;

    @Column(name = "claim_org", length = 200, nullable = false)
    private String claimOrg;

    @Column(name = "facts_and_reasons", length = 2000, nullable = false)
    private String factsAndReasons;

    @Column(name = "prosecution_probability", length = 20, nullable = false)
    private String prosecutionProbability;

    @Column(name = "lose_probability", length = 20, nullable = false)
    private String loseProbability;

    @Column(name = "attachments", length = 2000)
    private String attachments;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", length = 30, nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_path", length = 30)
    private ApprovalPath approvalPath;

    @Column(name = "created_by", length = 32)
    private String createdBy;

    @Column(name = "created_by_name", length = 50)
    private String createdByName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString().replace("-", "");
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.approvalStatus == null) {
            this.approvalStatus = ApprovalStatus.DRAFT;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getTaskCode() { return taskCode; }
    public void setTaskCode(String taskCode) { this.taskCode = taskCode; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getCourtDocumentNo() { return courtDocumentNo; }
    public void setCourtDocumentNo(String courtDocumentNo) { this.courtDocumentNo = courtDocumentNo; }

    public String getIsMajorCase() { return isMajorCase; }
    public void setIsMajorCase(String isMajorCase) { this.isMajorCase = isMajorCase; }

    public String getIsClaimPaymentDispute() { return isClaimPaymentDispute; }
    public void setIsClaimPaymentDispute(String isClaimPaymentDispute) { this.isClaimPaymentDispute = isClaimPaymentDispute; }

    public String getIsRefundDispute() { return isRefundDispute; }
    public void setIsRefundDispute(String isRefundDispute) { this.isRefundDispute = isRefundDispute; }

    public String getOccurrencePeriod() { return occurrencePeriod; }
    public void setOccurrencePeriod(String occurrencePeriod) { this.occurrencePeriod = occurrencePeriod; }

    public String getClaimedOrgProvince() { return claimedOrgProvince; }
    public void setClaimedOrgProvince(String claimedOrgProvince) { this.claimedOrgProvince = claimedOrgProvince; }

    public String getClaimedOrgCity() { return claimedOrgCity; }
    public void setClaimedOrgCity(String claimedOrgCity) { this.claimedOrgCity = claimedOrgCity; }

    public String getClaimedOrgDistrict() { return claimedOrgDistrict; }
    public void setClaimedOrgDistrict(String claimedOrgDistrict) { this.claimedOrgDistrict = claimedOrgDistrict; }

    public String getClaimedOrgOther() { return claimedOrgOther; }
    public void setClaimedOrgOther(String claimedOrgOther) { this.claimedOrgOther = claimedOrgOther; }

    public String getClaimOrg() { return claimOrg; }
    public void setClaimOrg(String claimOrg) { this.claimOrg = claimOrg; }

    public String getFactsAndReasons() { return factsAndReasons; }
    public void setFactsAndReasons(String factsAndReasons) { this.factsAndReasons = factsAndReasons; }

    public String getProsecutionProbability() { return prosecutionProbability; }
    public void setProsecutionProbability(String prosecutionProbability) { this.prosecutionProbability = prosecutionProbability; }

    public String getLoseProbability() { return loseProbability; }
    public void setLoseProbability(String loseProbability) { this.loseProbability = loseProbability; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }

    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) { this.approvalStatus = approvalStatus; }

    public ApprovalPath getApprovalPath() { return approvalPath; }
    public void setApprovalPath(ApprovalPath approvalPath) { this.approvalPath = approvalPath; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
