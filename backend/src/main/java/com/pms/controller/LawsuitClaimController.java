package com.pms.controller;

import com.pms.dto.*;
import com.pms.entity.LawsuitClaimApprovalLog;
import com.pms.service.LawsuitClaimService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lawsuit-claims")
public class LawsuitClaimController {

    private final LawsuitClaimService lawsuitClaimService;

    private static final String UPLOAD_DIR = "uploads/lawsuit-claims/";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public LawsuitClaimController(LawsuitClaimService lawsuitClaimService) {
        this.lawsuitClaimService = lawsuitClaimService;
    }

    @PostMapping
    public ApiResponse<LawsuitClaimVO> create(@Valid @RequestBody LawsuitClaimAddRequest request,
                                               Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "unknown";
        return ApiResponse.success(lawsuitClaimService.create(request, userId, userId));
    }

    @GetMapping
    public ApiResponse<PageResult<LawsuitClaimVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String approvalStatus) {
        return ApiResponse.success(lawsuitClaimService.list(page, size, keyword, approvalStatus));
    }

    @GetMapping("/{id}")
    public ApiResponse<LawsuitClaimVO> getById(@PathVariable String id) {
        return ApiResponse.success(lawsuitClaimService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<LawsuitClaimVO> update(@PathVariable String id,
                                               @Valid @RequestBody LawsuitClaimUpdateRequest request) {
        return ApiResponse.success(lawsuitClaimService.update(id, request));
    }

    @PostMapping("/{id}/approve")
    public ApiResponse<LawsuitClaimVO> approve(@PathVariable String id,
                                                @Valid @RequestBody ApprovalRequest request,
                                                Authentication authentication) {
        String operatorId = authentication != null ? authentication.getName() : "unknown";
        return ApiResponse.success(lawsuitClaimService.approve(id, request, operatorId, operatorId));
    }

    @GetMapping("/{id}/approval-logs")
    public ApiResponse<List<LawsuitClaimApprovalLog>> getApprovalLogs(@PathVariable String id) {
        return ApiResponse.success(lawsuitClaimService.getApprovalLogs(id));
    }

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "文件不能为空");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return ApiResponse.error(400, "文件大小不能超过10MB");
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;

            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            return ApiResponse.success(UPLOAD_DIR + newFilename);
        } catch (IOException e) {
            return ApiResponse.error(500, "文件上传失败: " + e.getMessage());
        }
    }
}
