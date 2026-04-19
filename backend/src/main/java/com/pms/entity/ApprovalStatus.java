package com.pms.entity;

public enum ApprovalStatus {
    DRAFT("草稿"),
    PENDING_PROVINCE_OFFICE_REVIEW("待省经办审批"),
    PENDING_PROVINCE_FINAL_REVIEW("待省审核"),
    PENDING_OFFICE_REVIEW("待总经办审批"),
    PENDING_FINAL_REVIEW("待总审核"),
    APPROVED("已通过"),
    REJECTED("已驳回");

    private final String label;

    ApprovalStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
