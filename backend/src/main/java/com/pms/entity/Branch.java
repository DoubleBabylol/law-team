package com.pms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_branch")
public class Branch {

    @Id
    @Column(name = "branch_no", length = 6, nullable = false)
    private String branchNo;

    @Column(name = "branch_name", length = 100, nullable = false)
    private String branchName;

    @Column(name = "prio_branch_no", length = 6)
    private String prioBranchNo;

    @Column(name = "level", nullable = false)
    private Integer level;

    public String getBranchNo() { return branchNo; }
    public void setBranchNo(String branchNo) { this.branchNo = branchNo; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getPrioBranchNo() { return prioBranchNo; }
    public void setPrioBranchNo(String prioBranchNo) { this.prioBranchNo = prioBranchNo; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
}
