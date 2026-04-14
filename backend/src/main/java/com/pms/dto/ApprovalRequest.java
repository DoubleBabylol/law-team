package com.pms.dto;

import jakarta.validation.constraints.NotBlank;

public class ApprovalRequest {

    @NotBlank(message = "操作类型不能为空")
    private String action; // SUBMIT, APPROVE, REJECT

    private String comment;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
