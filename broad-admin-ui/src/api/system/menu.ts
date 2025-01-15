import { request } from '@/api/request'
import type { MenuData } from '@/views/system/menu/types'

export function getMenus(params?: any) {
  return request.get({
    url: '/sysMenu',
    data: params
  })
}

export function getMenusTree(params?: any) {
  return request.get({
    url: '/sysMenu/tree',
    data: params
  })
}

export function getMenuChild(params: { menuId: string | number }) {
  return request.get({
    url: '/sysMenu/getMenuChild',
    data: params
  })
}

export function addMenus(data: Partial<MenuData>) {
  return request.post({
    url: '/sysMenu',
    data
  })
}

export function updateMenus(data: Partial<MenuData>) {
  return request.put({
    url: '/sysMenu',
    data
  })
}

export function deleteMenus(params: { menuId: string | number }) {
  return request.delete({
    url: '/sysMenu',
    data: params
  })
}
