import request from './request'
import type { ApiResponse, Branch } from '@/types'

export function getBranchList(): Promise<ApiResponse<Branch[]>> {
  return request.get('/api/branch/list')
}

export function getBranchTree(): Promise<ApiResponse<Branch[]>> {
  return request.get('/api/branch/tree')
}
