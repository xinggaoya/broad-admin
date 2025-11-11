import { request } from '@/api/request'

const BASE_URL = '/system/user'

export const getUserPage = (data: Record<string, any>) => {
  return request.get({
    url: `${BASE_URL}/page`,
    data
  })
}

export const getUserMeta = () => {
  return request.get({
    url: `${BASE_URL}/meta`
  })
}

export const addUser = (data: Record<string, any>) => {
  return request.post({
    url: BASE_URL,
    data
  })
}

export const updateUser = (data: Record<string, any>) => {
  return request.put({
    url: BASE_URL,
    data
  })
}

export const delUser = (ids: number[] | number) => {
  const target = Array.isArray(ids) ? ids : [ids]
  return request.delete({
    url: BASE_URL,
    data: { ids: target }
  })
}

export const updateUserStatusApi = (data: { id: number; userStatus: string }) => {
  return request.put({
    url: `${BASE_URL}/status`,
    data
  })
}

export const resetUserPasswordApi = (data: { id: number; password: string }) => {
  return request.put({
    url: `${BASE_URL}/password`,
    data
  })
}

export const getUserInfo = (userId: string | number) => {
  return request.get({
    url: `${BASE_URL}/${userId}`
  })
}
