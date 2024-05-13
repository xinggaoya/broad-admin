import { createAxios } from '@/api/request'

export const getRoleMenu = (data?: any) => {
  return createAxios({
    url: '/sysRoleMenu',
    method: 'GET',
    data,
  })
}
export const addRoleMenu = (data?: any) => {
  return createAxios({
    url: '/sysRoleMenu',
    method: 'POST',
    data,
  })
}
export const delRoleMenu = (data?: any) => {
  return createAxios({
    url: '/sysRoleMenu',
    method: 'DELETE',
    data,
  })
}
