## 修改需求

### 需求:审批操作接口

系统必须提供 POST /api/lawsuit-claims/{id}/approve 接口，用于执行审批操作（提交、通过、驳回）。系统必须根据索赔事项的审批路径类型（MAJOR_CLAIM 或 PROVINCE_ENTRY）执行不同的状态流转逻辑。

#### 场景:省级录入路径提交审批

- **当** 客户端发送 action=SUBMIT 的审批请求，事项审批路径为 PROVINCE_ENTRY，状态为 DRAFT 或 REJECTED
- **那么** 系统必须将状态更新为 PENDING_PROVINCE_OFFICE_REVIEW 并记录审批日志

#### 场景:重大索赔路径提交审批

- **当** 客户端发送 action=SUBMIT 的审批请求，事项审批路径为 MAJOR_CLAIM 或为空，状态为 DRAFT 或 REJECTED
- **那么** 系统必须将状态更新为 PENDING_OFFICE_REVIEW 并记录审批日志（保持原有行为不变）

#### 场景:省级录入路径通过审批

- **当** 客户端发送 action=APPROVE 的审批请求，事项审批路径为 PROVINCE_ENTRY
- **那么** 系统必须按路径流转：PENDING_PROVINCE_OFFICE_REVIEW → PENDING_PROVINCE_FINAL_REVIEW → APPROVED，并记录日志

#### 场景:省级录入路径驳回审批

- **当** 客户端发送 action=REJECT 的审批请求，事项状态为 PENDING_PROVINCE_OFFICE_REVIEW 或 PENDING_PROVINCE_FINAL_REVIEW
- **那么** 系统必须将状态更新为 REJECTED 并记录审批日志

#### 场景:非法状态操作

- **当** 客户端发送审批请求，但当前状态不允许该操作
- **那么** 系统必须返回 400 状态码，提示当前状态不允许该操作

### 需求:创建索赔事项接口

系统必须提供 POST /api/lawsuit-claims 接口，支持传入审批路径类型字段（approvalPath）。

#### 场景:创建省级录入审批路径事项

- **当** 客户端发送创建请求，包含 approvalPath=PROVINCE_ENTRY
- **那么** 系统必须创建索赔事项，审批路径记录为 PROVINCE_ENTRY，状态为 DRAFT
