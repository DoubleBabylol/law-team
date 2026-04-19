package com.pms.entity;

public enum ApprovalPath {
    MAJOR_CLAIM("重大索赔审批"),
    PROVINCE_ENTRY("省级录入审批");

    private final String label;

    ApprovalPath(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
