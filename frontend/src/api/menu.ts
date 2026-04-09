import request from './request'
import type { ApiResponse, Menu } from '@/types'

export function getMenuTree(): Promise<ApiResponse<Menu[]>> {
  return request.get('/api/menu/tree')
}

export function getMenuList(): Promise<ApiResponse<Menu[]>> {
  return request.get('/api/menu/list')
}

export function addMenu(data: Partial<Menu>): Promise<ApiResponse<Menu>> {
  return request.post('/api/menu/add', data)
}

export function updateMenu(id: string, data: Partial<Menu>): Promise<ApiResponse<Menu>> {
  return request.put(`/api/menu/update/${id}`, data)
}

export function deleteMenu(id: string): Promise<ApiResponse<void>> {
  return request.delete(`/api/menu/delete/${id}`)
}
