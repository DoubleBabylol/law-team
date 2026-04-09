package com.pms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sys_user")
public class User {

    @Id
    @Column(name = "user_id", length = 32, nullable = false)
    private String userId;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "clerk_no", length = 20, unique = true, nullable = false)
    private String clerkNo;

    @Column(name = "branch_no", length = 6, nullable = false)
    private String branchNo;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Column(name = "dept_name", length = 50)
    private String deptName;

    @Column(name = "dept_no", length = 10)
    private String deptNo;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        if (this.userId == null || this.userId.isEmpty()) {
            this.userId = UUID.randomUUID().toString().replace("-", "");
        }
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getClerkNo() { return clerkNo; }
    public void setClerkNo(String clerkNo) { this.clerkNo = clerkNo; }

    public String getBranchNo() { return branchNo; }
    public void setBranchNo(String branchNo) { this.branchNo = branchNo; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getDeptNo() { return deptNo; }
    public void setDeptNo(String deptNo) { this.deptNo = deptNo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
