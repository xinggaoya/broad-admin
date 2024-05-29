import { request } from '@/api/request'

export function getMenus(data: any) {
  return request.get({
    url: '/sysMenu',
    data,
  })
}
export function getMenusTree(data?: any) {
  return request.get({
    url: '/sysMenu/tree',
    data,
  })
}

export function getMenuChild(data: any) {
  return request.get({
    url: '/sysMenu/getMenuChild',
    data,
  })
}

export function addMenus(data: any) {
  return request.post({
    url: '/sysMenu',
    data,
  })
}

export function updateMenus(data: any) {
  return request.put({
    url: '/sysMenu',
    data,
  })
}

export function deleteMenus(data: any) {
  return request.delete({
    url: '/sysMenu',
    data,
  })
}
