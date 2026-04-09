import request from './request'
import type { ApiResponse, Menu } from '@/types'

export function getRoleMenus(roleId: string): Promise<ApiResponse<Menu[]>> {
  return request.get(`/api/role-permission/menus/${roleId}`)
}

export function saveRoleMenus(roleId: string, menuIds: string[]): Promise<ApiResponse<void>> {
  return request.post(`/api/role-permission/save-menus/${roleId}`, menuIds)
}
