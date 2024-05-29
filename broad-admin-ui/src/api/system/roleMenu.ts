import { request } from '@/api/request'

export const getRoleMenu = (data?: any) => {
  return request.get({
    url: '/sysRoleMenu',
    data,
  })
}
export const addRoleMenu = (data?: any) => {
  return request.post({
    url: '/sysRoleMenu',
    data,
  })
}
export const delRoleMenu = (data?: any) => {
  return request.delete({
    url: '/sysRoleMenu',
    data,
  })
}
