package com.pms.dto;

public class RoleDTO {
    private String roleId;
    private String roleName;
    private String roleGroup;
    private Boolean status;

    public RoleDTO() {
    }

    public RoleDTO(String roleId, String roleName, String roleGroup, Boolean status) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleGroup = roleGroup;
        this.status = status;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
