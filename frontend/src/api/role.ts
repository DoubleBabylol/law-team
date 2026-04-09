import request from './request'
import type { ApiResponse, Role } from '@/types'

export function getRoleList(): Promise<ApiResponse<Role[]>> {
  return request.get('/api/role/list')
}

export function getRoleDetail(id: string): Promise<ApiResponse<Role>> {
  return request.get(`/api/role/detail/${id}`)
}

export function addRole(data: Partial<Role>): Promise<ApiResponse<Role>> {
  return request.post('/api/role/add', data)
}

export function updateRole(id: string, data: Partial<Role>): Promise<ApiResponse<Role>> {
  return request.put(`/api/role/update/${id}`, data)
}

export function deleteRole(id: string): Promise<ApiResponse<void>> {
  return request.delete(`/api/role/delete/${id}`)
}
