package com.pms.dto;

import com.pms.entity.ApprovalStatus;
import com.pms.entity.ApprovalPath;
import java.time.LocalDateTime;

public class LawsuitClaimVO {

    private String id;
    private String taskName;
    private String taskCode;
    private String taskType;
    private String courtDocumentNo;
    private String isMajorCase;
    private String isClaimPaymentDispute;
    private String isRefundDispute;
    private String occurrencePeriod;
    private String claimedOrgProvince;
    private String claimedOrgCity;
    private String claimedOrgDistrict;
    private String claimedOrgOther;
    private String claimOrg;
    private String factsAndReasons;
    private String prosecutionProbability;
    private String loseProbability;
    private String attachments;
    private ApprovalStatus approvalStatus;
    private String approvalStatusLabel;
    private String approvalPath;
    private String approvalPathLabel;
    private String createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public String getApprovalStatusLabel() { return approvalStatusLabel; }
    public void setApprovalStatusLabel(String approvalStatusLabel) { this.approvalStatusLabel = approvalStatusLabel; }

    public String getApprovalPath() { return approvalPath; }
    public void setApprovalPath(String approvalPath) { this.approvalPath = approvalPath; }

    public String getApprovalPathLabel() { return approvalPathLabel; }
    public void setApprovalPathLabel(String approvalPathLabel) { this.approvalPathLabel = approvalPathLabel; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
