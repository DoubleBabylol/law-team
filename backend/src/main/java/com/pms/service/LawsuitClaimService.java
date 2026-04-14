package com.pms.service;

import com.pms.dto.*;
import com.pms.entity.ApprovalStatus;
import com.pms.entity.LawsuitClaim;
import com.pms.entity.LawsuitClaimApprovalLog;
import com.pms.exception.BusinessException;
import com.pms.repository.LawsuitClaimApprovalLogRepository;
import com.pms.repository.LawsuitClaimRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LawsuitClaimService {

    private final LawsuitClaimRepository lawsuitClaimRepository;
    private final LawsuitClaimApprovalLogRepository approvalLogRepository;

    public LawsuitClaimService(LawsuitClaimRepository lawsuitClaimRepository,
                                LawsuitClaimApprovalLogRepository approvalLogRepository) {
        this.lawsuitClaimRepository = lawsuitClaimRepository;
        this.approvalLogRepository = approvalLogRepository;
    }

    /**
     * 生成任务编码：诉讼[yyyy]第[00000]号
     */
    private synchronized String generateTaskCode() {
        int year = Year.now().getValue();
        String prefix = "诉讼[" + year + "]第";
        String likePrefix = prefix + "%";

        String maxCode = lawsuitClaimRepository.findMaxTaskCodeByPrefix(likePrefix).orElse(null);

        int nextSeq = 1;
        if (maxCode != null) {
            // 从 "诉讼[2026]第00001号" 中提取 00001
            int startIdx = maxCode.indexOf("第") + 1;
            int endIdx = maxCode.indexOf("号");
            if (startIdx > 0 && endIdx > startIdx) {
                String seqStr = maxCode.substring(startIdx, endIdx);
                nextSeq = Integer.parseInt(seqStr) + 1;
            }
        }

        return prefix + String.format("%05d", nextSeq) + "号";
    }

    @Transactional
    public LawsuitClaimVO create(LawsuitClaimAddRequest request, String userId, String username) {
        LawsuitClaim claim = new LawsuitClaim();
        claim.setTaskName(request.getTaskName());
        claim.setTaskCode(generateTaskCode());
        claim.setTaskType(request.getTaskType());
        claim.setCourtDocumentNo(request.getCourtDocumentNo());
        claim.setIsMajorCase(request.getIsMajorCase());
        claim.setIsClaimPaymentDispute(request.getIsClaimPaymentDispute());
        claim.setIsRefundDispute(request.getIsRefundDispute());
        claim.setOccurrencePeriod(request.getOccurrencePeriod());
        claim.setClaimedOrgProvince(request.getClaimedOrgProvince());
        claim.setClaimedOrgCity(request.getClaimedOrgCity());
        claim.setClaimedOrgDistrict(request.getClaimedOrgDistrict());
        claim.setClaimedOrgOther(request.getClaimedOrgOther());
        claim.setClaimOrg(request.getClaimOrg());
        claim.setFactsAndReasons(request.getFactsAndReasons());
        claim.setProsecutionProbability(request.getProsecutionProbability());
        claim.setLoseProbability(request.getLoseProbability());
        claim.setAttachments(request.getAttachments());
        claim.setApprovalStatus(ApprovalStatus.DRAFT);
        claim.setCreatedBy(userId);
        claim.setCreatedByName(username);

        LawsuitClaim saved = lawsuitClaimRepository.save(claim);
        return convertToVO(saved);
    }

    public PageResult<LawsuitClaimVO> list(int page, int size, String keyword, String approvalStatus) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<LawsuitClaim> claimPage;

        ApprovalStatus status = null;
        if (approvalStatus != null && !approvalStatus.trim().isEmpty()) {
            try {
                status = ApprovalStatus.valueOf(approvalStatus);
            } catch (IllegalArgumentException e) {
                // ignore invalid status
            }
        }

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        if (hasKeyword && status != null) {
            claimPage = lawsuitClaimRepository.searchByKeywordAndStatus(keyword.trim(), status, pageable);
        } else if (hasKeyword) {
            claimPage = lawsuitClaimRepository.searchByKeyword(keyword.trim(), pageable);
        } else if (status != null) {
            claimPage = lawsuitClaimRepository.findByApprovalStatus(status, pageable);
        } else {
            claimPage = lawsuitClaimRepository.findAll(pageable);
        }

        List<LawsuitClaimVO> records = claimPage.getContent().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(records, claimPage.getTotalElements(), page, size);
    }

    public LawsuitClaimVO getById(String id) {
        LawsuitClaim claim = lawsuitClaimRepository.findById(id)
                .orElseThrow(() -> new BusinessException("索赔事项不存在"));
        return convertToVO(claim);
    }

    @Transactional
    public LawsuitClaimVO update(String id, LawsuitClaimUpdateRequest request) {
        LawsuitClaim claim = lawsuitClaimRepository.findById(id)
                .orElseThrow(() -> new BusinessException("索赔事项不存在"));

        if (claim.getApprovalStatus() != ApprovalStatus.DRAFT
                && claim.getApprovalStatus() != ApprovalStatus.REJECTED) {
            throw new BusinessException("当前状态不允许编辑");
        }

        claim.setTaskName(request.getTaskName());
        claim.setTaskType(request.getTaskType());
        claim.setCourtDocumentNo(request.getCourtDocumentNo());
        claim.setIsMajorCase(request.getIsMajorCase());
        claim.setIsClaimPaymentDispute(request.getIsClaimPaymentDispute());
        claim.setIsRefundDispute(request.getIsRefundDispute());
        claim.setOccurrencePeriod(request.getOccurrencePeriod());
        claim.setClaimedOrgProvince(request.getClaimedOrgProvince());
        claim.setClaimedOrgCity(request.getClaimedOrgCity());
        claim.setClaimedOrgDistrict(request.getClaimedOrgDistrict());
        claim.setClaimedOrgOther(request.getClaimedOrgOther());
        claim.setClaimOrg(request.getClaimOrg());
        claim.setFactsAndReasons(request.getFactsAndReasons());
        claim.setProsecutionProbability(request.getProsecutionProbability());
        claim.setLoseProbability(request.getLoseProbability());
        claim.setAttachments(request.getAttachments());

        LawsuitClaim updated = lawsuitClaimRepository.save(claim);
        return convertToVO(updated);
    }

    @Transactional
    public LawsuitClaimVO approve(String id, ApprovalRequest request, String operatorId, String operatorName) {
        LawsuitClaim claim = lawsuitClaimRepository.findById(id)
                .orElseThrow(() -> new BusinessException("索赔事项不存在"));

        String action = request.getAction().toUpperCase();
        ApprovalStatus currentStatus = claim.getApprovalStatus();

        switch (action) {
            case "SUBMIT":
                if (currentStatus != ApprovalStatus.DRAFT && currentStatus != ApprovalStatus.REJECTED) {
                    throw new BusinessException("当前状态不允许提交审批");
                }
                claim.setApprovalStatus(ApprovalStatus.PENDING_OFFICE_REVIEW);
                break;

            case "APPROVE":
                if (currentStatus == ApprovalStatus.PENDING_OFFICE_REVIEW) {
                    claim.setApprovalStatus(ApprovalStatus.PENDING_FINAL_REVIEW);
                } else if (currentStatus == ApprovalStatus.PENDING_FINAL_REVIEW) {
                    claim.setApprovalStatus(ApprovalStatus.APPROVED);
                } else {
                    throw new BusinessException("当前状态不允许审批通过");
                }
                break;

            case "REJECT":
                if (currentStatus != ApprovalStatus.PENDING_OFFICE_REVIEW
                        && currentStatus != ApprovalStatus.PENDING_FINAL_REVIEW) {
                    throw new BusinessException("当前状态不允许驳回");
                }
                claim.setApprovalStatus(ApprovalStatus.REJECTED);
                break;

            default:
                throw new BusinessException("不支持的操作类型: " + action);
        }

        LawsuitClaim saved = lawsuitClaimRepository.save(claim);

        // 记录审批日志
        LawsuitClaimApprovalLog log = new LawsuitClaimApprovalLog();
        log.setLawsuitClaimId(id);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setActionType(action);
        log.setComment(request.getComment());
        approvalLogRepository.save(log);

        return convertToVO(saved);
    }

    public List<LawsuitClaimApprovalLog> getApprovalLogs(String claimId) {
        // 验证索赔事项存在
        lawsuitClaimRepository.findById(claimId)
                .orElseThrow(() -> new BusinessException("索赔事项不存在"));
        return approvalLogRepository.findByLawsuitClaimIdOrderByCreatedAtDesc(claimId);
    }

    private LawsuitClaimVO convertToVO(LawsuitClaim claim) {
        LawsuitClaimVO vo = new LawsuitClaimVO();
        vo.setId(claim.getId());
        vo.setTaskName(claim.getTaskName());
        vo.setTaskCode(claim.getTaskCode());
        vo.setTaskType(claim.getTaskType());
        vo.setCourtDocumentNo(claim.getCourtDocumentNo());
        vo.setIsMajorCase(claim.getIsMajorCase());
        vo.setIsClaimPaymentDispute(claim.getIsClaimPaymentDispute());
        vo.setIsRefundDispute(claim.getIsRefundDispute());
        vo.setOccurrencePeriod(claim.getOccurrencePeriod());
        vo.setClaimedOrgProvince(claim.getClaimedOrgProvince());
        vo.setClaimedOrgCity(claim.getClaimedOrgCity());
        vo.setClaimedOrgDistrict(claim.getClaimedOrgDistrict());
        vo.setClaimedOrgOther(claim.getClaimedOrgOther());
        vo.setClaimOrg(claim.getClaimOrg());
        vo.setFactsAndReasons(claim.getFactsAndReasons());
        vo.setProsecutionProbability(claim.getProsecutionProbability());
        vo.setLoseProbability(claim.getLoseProbability());
        vo.setAttachments(claim.getAttachments());
        vo.setApprovalStatus(claim.getApprovalStatus());
        vo.setApprovalStatusLabel(claim.getApprovalStatus().getLabel());
        vo.setCreatedBy(claim.getCreatedBy());
        vo.setCreatedByName(claim.getCreatedByName());
        vo.setCreatedAt(claim.getCreatedAt());
        vo.setUpdatedAt(claim.getUpdatedAt());
        return vo;
    }
}
