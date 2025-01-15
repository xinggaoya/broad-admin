import { request } from '@/api/request'
import type { ApiResponse, MenuData } from '@/views/system/menu/types'

export function getMenus(params?: any) {
  return request.get<ApiResponse<MenuData[]>>({
    url: '/sysMenu',
    params
  })
}

export function getMenusTree(params?: any) {
  return request.get<ApiResponse<MenuData[]>>({
    url: '/sysMenu/tree',
    params
  })
}

export function getMenuChild(params: { menuId: string | number }) {
  return request.get<ApiResponse<MenuData[]>>({
    url: '/sysMenu/getMenuChild',
    params
  })
}

export function addMenus(data: Partial<MenuData>) {
  return request.post<ApiResponse<any>>({
    url: '/sysMenu',
    data
  })
}

export function updateMenus(data: Partial<MenuData>) {
  return request.put<ApiResponse<any>>({
    url: '/sysMenu',
    data
  })
}

export function deleteMenus(params: { menuId: string | number }) {
  return request.delete<ApiResponse<any>>({
    url: '/sysMenu',
    params
  })
}
