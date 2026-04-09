package com.pms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sys_role")
public class Role {

    @Id
    @Column(name = "role_id", length = 32, nullable = false)
    private String roleId;

    @Column(name = "role_name", length = 50, unique = true, nullable = false)
    private String roleName;

    @Column(name = "role_group", length = 20, nullable = false)
    private String roleGroup;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        if (this.roleId == null || this.roleId.isEmpty()) {
            this.roleId = UUID.randomUUID().toString().replace("-", "");
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

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getRoleGroup() { return roleGroup; }
    public void setRoleGroup(String roleGroup) { this.roleGroup = roleGroup; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
