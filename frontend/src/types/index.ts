// 统一响应
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 分页响应
export interface PageResult<T = any> {
  records: T[]
  total: number
  page: number
  size: number
}

// 用户
export interface User {
  userId: string
  username: string
  clerkNo: string
  branchNo: string
  branchName: string
  deptName: string
  deptNo: string
  status: boolean
  createTime: string
  updateTime: string
  roles?: Role[]
  roleIds?: string[]
}

// 角色
export interface Role {
  roleId: string
  roleName: string
  roleGroup: string
  status: boolean
  createTime: string
  updateTime: string
}

// 菜单（树形）
export interface Menu {
  resourceId: string
  parentResourceId: string | null
  resourceName: string
  resourceType: string  // 目录/菜单/按钮
  resourcePath: string
  permission: string
  icon: string
  sortOrder: number
  status: boolean
  createTime: string
  updateTime: string
  children?: Menu[]
}

// 机构
export interface Branch {
  branchNo: string
  branchName: string
  prioBranchNo: string
  level: number
}

// 登录
export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  userId: string
  username: string
  roles: Role[]
  menus: Menu[]
}

// 诉讼索赔事项
export interface LawsuitClaim {
  id: string
  taskName: string
  taskCode: string
  taskType: string
  courtDocumentNo: string
  isMajorCase: string
  isClaimPaymentDispute: string
  isRefundDispute: string
  occurrencePeriod: string
  claimedOrgProvince: string
  claimedOrgCity: string
  claimedOrgDistrict: string
  claimedOrgOther: string
  claimOrg: string
  factsAndReasons: string
  prosecutionProbability: string
  loseProbability: string
  attachments: string
  approvalStatus: string
  approvalStatusLabel: string
  createdBy: string
  createdByName: string
  createdAt: string
  updatedAt: string
}

// 审批日志
export interface ApprovalLog {
  id: string
  lawsuitClaimId: string
  operatorId: string
  operatorName: string
  actionType: string
  comment: string
  createdAt: string
}
