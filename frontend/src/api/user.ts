import request from './request'
import type { ApiResponse, PageResult, User } from '@/types'

interface UserListParams {
  page: number
  size: number
  keyword?: string
}

export function getUserList(params: UserListParams): Promise<ApiResponse<PageResult<User>>> {
  return request.get('/api/user/list', { params })
}

export function getUserDetail(id: string): Promise<ApiResponse<User>> {
  return request.get(`/api/user/detail/${id}`)
}

export function addUser(data: Partial<User>): Promise<ApiResponse<User>> {
  return request.post('/api/user/add', data)
}

export function updateUser(id: string, data: Partial<User>): Promise<ApiResponse<User>> {
  return request.put(`/api/user/update/${id}`, data)
}

export function deleteUser(id: string): Promise<ApiResponse<void>> {
  return request.delete(`/api/user/delete/${id}`)
}

export function resetPassword(id: string): Promise<ApiResponse<void>> {
  return request.put(`/api/user/reset-password/${id}`)
}
