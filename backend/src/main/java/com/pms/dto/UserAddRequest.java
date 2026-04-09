package com.pms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class UserAddRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "工号不能为空")
    private String clerkNo;

    @NotBlank(message = "机构号不能为空")
    private String branchNo;

    private String branchName;

    private String deptName;

    private String deptNo;

    @NotNull(message = "角色不能为空")
    private List<String> roleIds;

    @NotNull(message = "状态不能为空")
    private Boolean status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClerkNo() {
        return clerkNo;
    }

    public void setClerkNo(String clerkNo) {
        this.clerkNo = clerkNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
