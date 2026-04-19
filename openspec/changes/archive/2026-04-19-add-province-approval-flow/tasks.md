## 1. 后端实体与枚举扩展

- [x] 1.1 在 ApprovalStatus 枚举中新增 PENDING_PROVINCE_OFFICE_REVIEW("待省经办审批") 和 PENDING_PROVINCE_FINAL_REVIEW("待省审核")
- [x] 1.2 新建 ApprovalPath 枚举（MAJOR_CLAIM、PROVINCE_ENTRY），用于区分审批路径类型
- [x] 1.3 在 LawsuitClaim 实体中新增 approvalPath 字段（ApprovalPath 类型，可为 null，默认视为 MAJOR_CLAIM）

## 2. 后端审批逻辑扩展

- [x] 2.1 修改 LawsuitClaimService.approve() 的 SUBMIT 操作：根据 approvalPath 字段决定进入 PENDING_OFFICE_REVIEW（重大索赔）还是 PENDING_PROVINCE_OFFICE_REVIEW（省级录入）
- [x] 2.2 在 APPROVE 操作中新增省级路径流转：PENDING_PROVINCE_OFFICE_REVIEW → PENDING_PROVINCE_FINAL_REVIEW → APPROVED
- [x] 2.3 在 REJECT 操作中新增对 PENDING_PROVINCE_OFFICE_REVIEW 和 PENDING_PROVINCE_FINAL_REVIEW 状态的驳回支持
- [x] 2.4 确保原有重大索赔审批路径（PENDING_OFFICE_REVIEW → PENDING_FINAL_REVIEW → APPROVED）逻辑完全不变

## 3. 后端 API 适配

- [x] 3.1 在 LawsuitClaimAddRequest DTO 中新增 approvalPath 字段
- [x] 3.2 创建索赔事项时将 approvalPath 字段持久化到数据库
- [x] 3.3 在 LawsuitClaimVO 中返回 approvalPath 字段

## 4. 前端状态适配

- [x] 4.1 在前端类型定义中新增两个省级审批状态值及对应中文标签映射
- [x] 4.2 新增 approvalPath 字段到前端类型定义
- [x] 4.3 更新 LawsuitClaimList 页面的状态筛选选项，增加省级审批状态
- [x] 4.4 更新 LawsuitClaimForm 页面，支持选择审批路径类型
- [x] 4.5 更新审批操作按钮逻辑，适配省级审批状态下的可用操作

## 5. 验证测试

- [x] 5.1 验证省级录入路径：提交 → 省经办通过 → 省审核通过 → 已通过
- [x] 5.2 验证省级录入路径的驳回和重新提交
- [x] 5.3 验证原有重大索赔路径完全不受影响：提交 → 总经办通过 → 总审核通过 → 已通过
- [x] 5.4 验证已有数据（approvalPath 为 null）默认走重大索赔路径
