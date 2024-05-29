import { request } from '@/api/request'

export const getUserPage = (data: any) => {
  return request.get({
    url: '/sysAdmin',
    data,
  })
}
export const getUserRole = (id: number) => {
  return request.get({
    url: '/sysUserRole/' + id,
  })
}
export const addUser = (data: any) => {
  return request.post({
    url: '/sysAdmin',
    data,
  })
}
export const updateUser = (data: any) => {
  return request.put({
    url: '/sysAdmin',
    data,
  })
}
export const delUser = (data: any) => {
  return request.delete({
    url: '/sysAdmin',
    data,
  })
}
