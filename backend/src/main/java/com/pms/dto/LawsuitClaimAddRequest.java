package com.pms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LawsuitClaimAddRequest {

    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    @Size(max = 50, message = "法院文书号码不能超过50个字符")
    private String courtDocumentNo;

    @NotBlank(message = "是否重大案件不能为空")
    private String isMajorCase;

    @NotBlank(message = "是否理赔给付纠纷不能为空")
    private String isClaimPaymentDispute;

    @NotBlank(message = "是否退保纠纷不能为空")
    private String isRefundDispute;

    @NotBlank(message = "发生区间不能为空")
    private String occurrencePeriod;

    @NotBlank(message = "被索赔机构-省不能为空")
    private String claimedOrgProvince;

    @NotBlank(message = "被索赔机构-市不能为空")
    private String claimedOrgCity;

    @NotBlank(message = "被索赔机构-区/县不能为空")
    private String claimedOrgDistrict;

    private String claimedOrgOther;

    @NotBlank(message = "索赔机构不能为空")
    private String claimOrg;

    @NotBlank(message = "事实与理由不能为空")
    @Size(max = 1000, message = "事实与理由不能超过1000个字符")
    private String factsAndReasons;

    @NotBlank(message = "起诉可能性评估不能为空")
    private String prosecutionProbability;

    @NotBlank(message = "败诉可能性评估不能为空")
    private String loseProbability;

    private String attachments;

    private String approvalPath; // MAJOR_CLAIM or PROVINCE_ENTRY

    // Getters and Setters
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

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

    public String getApprovalPath() { return approvalPath; }
    public void setApprovalPath(String approvalPath) { this.approvalPath = approvalPath; }
}
