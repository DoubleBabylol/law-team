## 1. 后端实体与数据库

- [x] 1.1 创建 ApprovalStatus 枚举类（DRAFT, PENDING_OFFICE_REVIEW, PENDING_FINAL_REVIEW, APPROVED, REJECTED），路径：`backend/src/main/java/com/pms/entity/ApprovalStatus.java`
- [x] 1.2 创建 LawsuitClaim 实体类，包含所有业务字段（taskName, taskCode, taskType, courtDocumentNo, isMajorCase, isClaimPaymentDispute, isRefundDispute, occurrencePeriod, claimedOrgProvince, claimedOrgCity, claimedOrgDistrict, claimedOrgOther, claimOrg, factsAndReasons, prosecutionProbability, loseProbability, attachments, approvalStatus, createdBy, createdAt, updatedAt），路径：`backend/src/main/java/com/pms/entity/LawsuitClaim.java`
- [x] 1.3 创建 LawsuitClaimApprovalLog 实体类（id, lawsuitClaimId, operatorId, operatorName, actionType, comment, createdAt），路径：`backend/src/main/java/com/pms/entity/LawsuitClaimApprovalLog.java`
- [x] 1.4 在 `data.sql` 中添加 lawsuit_claim 和 lawsuit_claim_approval_log 表的建表语句

## 2. 后端 Repository 层

- [x] 2.1 创建 LawsuitClaimRepository 接口，包含按年份查询最大任务编码的方法，路径：`backend/src/main/java/com/pms/repository/LawsuitClaimRepository.java`
- [x] 2.2 创建 LawsuitClaimApprovalLogRepository 接口，包含按索赔事项 ID 查询日志的方法，路径：`backend/src/main/java/com/pms/repository/LawsuitClaimApprovalLogRepository.java`

## 3. 后端 DTO 层

- [x] 3.1 创建 LawsuitClaimAddRequest DTO（表单提交所有字段），路径：`backend/src/main/java/com/pms/dto/LawsuitClaimAddRequest.java`
- [x] 3.2 创建 LawsuitClaimUpdateRequest DTO，路径：`backend/src/main/java/com/pms/dto/LawsuitClaimUpdateRequest.java`
- [x] 3.3 创建 LawsuitClaimVO 展示对象，路径：`backend/src/main/java/com/pms/dto/LawsuitClaimVO.java`
- [x] 3.4 创建 ApprovalRequest DTO（action, comment 字段），路径：`backend/src/main/java/com/pms/dto/ApprovalRequest.java`

## 4. 后端 Service 层

- [x] 4.1 创建 LawsuitClaimService，实现任务编码自动生成逻辑（诉讼[yyyy]第[00000]号格式），路径：`backend/src/main/java/com/pms/service/LawsuitClaimService.java`
- [x] 4.2 在 Service 中实现创建索赔事项方法（自动生成编码，默认 DRAFT 状态）
- [x] 4.3 在 Service 中实现分页查询和条件筛选方法（支持按审批状态筛选）
- [x] 4.4 在 Service 中实现更新索赔事项方法（仅 DRAFT 和 REJECTED 状态允许编辑）
- [x] 4.5 在 Service 中实现审批操作方法（提交/通过/驳回状态流转 + 记录审批日志）
- [x] 4.6 在 Service 中实现审批日志查询方法

## 5. 后端 Controller 层

- [x] 5.1 创建 LawsuitClaimController，实现 POST /api/lawsuit-claims（创建）接口，路径：`backend/src/main/java/com/pms/controller/LawsuitClaimController.java`
- [x] 5.2 实现 GET /api/lawsuit-claims（分页列表查询）接口
- [x] 5.3 实现 GET /api/lawsuit-claims/{id}（详情查询）接口
- [x] 5.4 实现 PUT /api/lawsuit-claims/{id}（更新）接口
- [x] 5.5 实现 POST /api/lawsuit-claims/{id}/approve（审批操作）接口
- [x] 5.6 实现 GET /api/lawsuit-claims/{id}/approval-logs（审批日志查询）接口

## 6. 后端文件上传

- [x] 6.1 创建 FileUploadController 或在 LawsuitClaimController 中实现 POST /api/lawsuit-claims/upload 文件上传接口
- [x] 6.2 在 application.yml 中配置文件上传大小限制和存储路径

## 7. 前端 API 层

- [x] 7.1 创建 `frontend/src/api/lawsuitClaim.ts`，封装索赔事项相关所有 API 调用（CRUD、审批、文件上传）

## 8. 前端省市区数据

- [x] 8.1 在 `frontend/src/data/` 下创建中国行政区划 JSON 数据文件（省、市、区县三级）

## 9. 前端填报页面

- [x] 9.1 创建 `frontend/src/views/lawsuit/LawsuitClaimForm.vue` 填报页面，包含 Element Plus 表单布局和所有字段
- [x] 9.2 实现任务类型、是否类字段、可能性评估的下拉框选项
- [x] 9.3 实现发生区间下拉框（动态计算当季及前5年季度）
- [x] 9.4 实现被索赔机构省市区三级联动选择器
- [x] 9.5 实现"被索赔机构-其他"字段的条件显示逻辑
- [x] 9.6 实现表单必填验证规则
- [x] 9.7 实现文本字段长度限制（法院文书号码50字、事实与理由1000字）
- [x] 9.8 实现附件上传组件（支持多文件上传和删除）

## 10. 前端列表与审批页面

- [x] 10.1 创建 `frontend/src/views/lawsuit/LawsuitClaimList.vue` 索赔事项列表页面，包含分页、状态筛选、操作按钮
- [x] 10.2 实现审批操作弹窗（通过/驳回，驳回需填写原因）
- [x] 10.3 实现审批日志查看弹窗

## 11. 前端路由与菜单

- [x] 11.1 在 `frontend/src/router/index.ts` 中添加索赔事项相关路由（列表页、新增页、编辑页、详情页）
- [x] 11.2 在侧边菜单中添加"诉讼管理 > 重大索赔事项"菜单项
