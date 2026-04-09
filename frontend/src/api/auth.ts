import request from './request'
import type { ApiResponse, LoginRequest, LoginResponse } from '@/types'

export function login(data: LoginRequest): Promise<ApiResponse<LoginResponse>> {
  return request.post('/api/auth/login', data)
}

export function logout(): Promise<ApiResponse<void>> {
  return request.post('/api/auth/logout')
}
