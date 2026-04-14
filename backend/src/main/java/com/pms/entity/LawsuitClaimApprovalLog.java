package com.pms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lawsuit_claim_approval_log")
public class LawsuitClaimApprovalLog {

    @Id
    @Column(name = "id", length = 32, nullable = false)
    private String id;

    @Column(name = "lawsuit_claim_id", length = 32, nullable = false)
    private String lawsuitClaimId;

    @Column(name = "operator_id", length = 32)
    private String operatorId;

    @Column(name = "operator_name", length = 50)
    private String operatorName;

    @Column(name = "action_type", length = 30, nullable = false)
    private String actionType;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString().replace("-", "");
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLawsuitClaimId() { return lawsuitClaimId; }
    public void setLawsuitClaimId(String lawsuitClaimId) { this.lawsuitClaimId = lawsuitClaimId; }

    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
