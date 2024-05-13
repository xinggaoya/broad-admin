import { createAxios } from '@/api/request'

export const getUserPage = (data: any) => {
  return createAxios({
    url: '/sysAdmin',
    method: 'GET',
    data,
  })
}
export const getUserRole = (id: number) => {
  return createAxios({
    url: '/sysUserRole/' + id,
    method: 'GET',
  })
}
export const addUser = (data: any) => {
  return createAxios({
    url: '/sysAdmin',
    method: 'POST',
    data,
  })
}
export const updateUser = (data: any) => {
  return createAxios({
    url: '/sysAdmin',
    method: 'PUT',
    data,
  })
}
export const delUser = (data: any) => {
  return createAxios({
    url: '/sysAdmin',
    method: 'DELETE',
    data,
  })
}
