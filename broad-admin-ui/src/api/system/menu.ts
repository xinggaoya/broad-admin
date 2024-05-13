import { createAxios } from '@/api/request'

export function getMenus(data: any) {
  return createAxios({
    url: '/sysMenu',
    method: 'GET',
    data,
  })
}
export function getMenusTree(data?: any) {
  return createAxios({
    url: '/sysMenu/tree',
    method: 'GET',
    data,
  })
}

export function getMenuChild(data: any) {
  return createAxios({
    url: '/sysMenu/getMenuChild',
    method: 'GET',
    data,
  })
}

export function addMenus(data: any) {
  return createAxios({
    url: '/sysMenu',
    method: 'POST',
    data,
  })
}

export function updateMenus(data: any) {
  return createAxios({
    url: '/sysMenu',
    method: 'PUT',
    data,
  })
}

export function deleteMenus(data: any) {
  return createAxios({
    url: '/sysMenu',
    method: 'DELETE',
    data,
  })
}
