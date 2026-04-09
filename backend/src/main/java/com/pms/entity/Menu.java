package com.pms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sys_menu")
public class Menu {

    @Id
    @Column(name = "resource_id", length = 32, nullable = false)
    private String resourceId;

    @Column(name = "parent_resource_id", length = 32)
    private String parentResourceId;

    @Column(name = "resource_name", length = 50, nullable = false)
    private String resourceName;

    @Column(name = "resource_type", length = 10, nullable = false)
    private String resourceType;

    @Column(name = "resource_path", length = 200)
    private String resourcePath;

    @Column(name = "permission", length = 100)
    private String permission;

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        if (this.resourceId == null || this.resourceId.isEmpty()) {
            this.resourceId = UUID.randomUUID().toString().replace("-", "");
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

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getParentResourceId() { return parentResourceId; }
    public void setParentResourceId(String parentResourceId) { this.parentResourceId = parentResourceId; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getResourcePath() { return resourcePath; }
    public void setResourcePath(String resourcePath) { this.resourcePath = resourcePath; }

    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
