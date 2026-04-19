## 上下文

当前系统实现了一条审批路径（总经办审批 → 总审核），审批状态通过 `ApprovalStatus` 枚举管理，状态流转逻辑集中在 `LawsuitClaimService.approve()` 方法中。现需新增一条独立的省级录入审批路径，与现有重大索赔审批路径并行存在。

现有审批路径（重大索赔）：DRAFT → PENDING_OFFICE_REVIEW → PENDING_FINAL_REVIEW → APPROVED

新增审批路径（省级录入）：DRAFT → PENDING_PROVINCE_OFFICE_REVIEW → PENDING_PROVINCE_FINAL_REVIEW → APPROVED

## 目标 / 非目标

**目标：**
- 新增独立的省级录入审批流程（省经办 → 省审核）
- 保持现有重大索赔审批流程完全不变（总经办 → 总审核）
- 保持现有 API 接口不变（POST /api/lawsuit-claims/{id}/approve）
- 审批日志完整记录省级审批操作
- 前端正确展示新增的审批状态

**非目标：**
- 不修改现有重大索赔审批流程的任何逻辑
- 不实现基于角色的审批权限控制
- 不实现审批流程的动态配置

## 决策

### 决策 1：两条审批路径的区分方式

**选择**：在 `LawsuitClaim` 实体中新增 `approvalPath` 字段（枚举类型：MATJOR_CLAIM / PROVINCE_ENTRY），用于区分索赔事项走哪条审批路径。

**理由**：显式标记审批路径，使服务层逻辑清晰，且便于后续扩展更多审批路径。

**替代方案**：
- 根据当前状态推断路径（状态不重叠时可行，但语义不够清晰）
- 创建两个独立的审批表（过度设计，破坏现有结构）

### 决策 2：状态枚举扩展

**选择**：在 `ApprovalStatus` 枚举中新增 `PENDING_PROVINCE_OFFICE_REVIEW` 和 `PENDING_PROVINCE_FINAL_REVIEW` 两个值。

**理由**：SQLite 中枚举以字符串存储，新增值不影响现有数据。最小变更方式。

### 决策 3：流转逻辑分离

**选择**：在 `approve()` 方法中根据 `approvalPath` 字段分别处理两条路径的流转逻辑。SUBMIT 时根据路径类型决定进入 PENDING_OFFICE_REVIEW 还是 PENDING_PROVINCE_OFFICE_REVIEW。

**理由**：保持现有代码结构，仅扩展 switch-case 逻辑即可。

## 风险 / 权衡

- **[数据兼容性]** 已有数据的 `approvalPath` 字段为空，需默认视为 MAJOR_CLAIM 路径 → 服务层判断时做 null 兼容处理
- **[前端状态展示]** 新增两个状态需要前端适配标签显示和按钮逻辑 → 修改前端状态映射
- **[路径确定时机]** 需在创建或提交时确定审批路径 → 创建时由前端传入或根据业务规则自动判断
