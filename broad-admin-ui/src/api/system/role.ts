import { request } from '@/api/request'

export const getRolePage = (data?: any) => {
  return request.get({
    url: '/sysRole',
    data,
  })
}
export const addRole = (data?: any) => {
  return request.post({
    url: '/sysRole',
    data,
  })
}
export const updateRole = (data?: any) => {
  return request.put({
    url: '/sysRole',
    data,
  })
}
export const delRole = (data?: any) => {
  return request.delete({
    url: '/sysRole/' + data,
  })
}
