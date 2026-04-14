import request from './request'
import type { ApiResponse, PageResult } from '@/types'
import type { LawsuitClaim, ApprovalLog } from '@/types'

interface LawsuitClaimListParams {
  page: number
  size: number
  keyword?: string
  approvalStatus?: string
}

export function getLawsuitClaimList(params: LawsuitClaimListParams): Promise<ApiResponse<PageResult<LawsuitClaim>>> {
  return request.get('/api/lawsuit-claims', { params })
}

export function getLawsuitClaimDetail(id: string): Promise<ApiResponse<LawsuitClaim>> {
  return request.get(`/api/lawsuit-claims/${id}`)
}

export function createLawsuitClaim(data: Partial<LawsuitClaim>): Promise<ApiResponse<LawsuitClaim>> {
  return request.post('/api/lawsuit-claims', data)
}

export function updateLawsuitClaim(id: string, data: Partial<LawsuitClaim>): Promise<ApiResponse<LawsuitClaim>> {
  return request.put(`/api/lawsuit-claims/${id}`, data)
}

export function approveLawsuitClaim(id: string, data: { action: string; comment?: string }): Promise<ApiResponse<LawsuitClaim>> {
  return request.post(`/api/lawsuit-claims/${id}/approve`, data)
}

export function getApprovalLogs(id: string): Promise<ApiResponse<ApprovalLog[]>> {
  return request.get(`/api/lawsuit-claims/${id}/approval-logs`)
}

export function uploadFile(file: File): Promise<ApiResponse<string>> {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/lawsuit-claims/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
