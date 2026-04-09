package com.pms.controller;

import com.pms.dto.ApiResponse;
import com.pms.entity.Branch;
import com.pms.repository.BranchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/branch")
public class BranchController {

    private final BranchRepository branchRepository;

    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @GetMapping("/list")
    public ApiResponse<List<Branch>> list() {
        // 返回所有机构列表（排除总公司，即 level != 1）
        List<Branch> branches = branchRepository.findAll().stream()
                .filter(branch -> branch.getLevel() != null && branch.getLevel() != 1)
                .collect(Collectors.toList());
        return ApiResponse.success(branches);
    }

    @GetMapping("/tree")
    public ApiResponse<List<Branch>> tree() {
        // 返回机构树形结构
        List<Branch> allBranches = branchRepository.findAll();
        return ApiResponse.success(buildBranchTree(allBranches));
    }

    private List<Branch> buildBranchTree(List<Branch> branches) {
        // 这里我们需要一个带有 children 的 DTO，但为了简单起见，我们直接返回平铺结构
        // 或者可以使用 Map 结构来组织树形
        // 由于 Branch 实体没有 children 字段，我们直接返回按层级排序的列表
        
        // 按 level 和 branchNo 排序
        return branches.stream()
                .sorted(Comparator
                        .comparing(Branch::getLevel, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(Branch::getBranchNo, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());
    }
}
