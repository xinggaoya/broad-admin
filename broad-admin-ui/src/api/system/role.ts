import { createAxios } from '@/api/request'

export const getRolePage = (data?: any) => {
  return createAxios({
    url: '/sysRole',
    method: 'GET',
    data,
  })
}
export const addRole = (data?: any) => {
  return createAxios({
    url: '/sysRole',
    method: 'POST',
    data,
  })
}
export const updateRole = (data?: any) => {
  return createAxios({
    url: '/sysRole',
    method: 'PUT',
    data,
  })
}
export const delRole = (data?: any) => {
  return createAxios({
    url: '/sysRole/' + data,
    method: 'DELETE',
  })
}
