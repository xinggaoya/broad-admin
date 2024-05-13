import { createAxios } from '@/api/request'

export const getOnline = (data: any) => {
  return createAxios({
    url: '/monitor/online',
    method: 'GET',
    data,
  })
}

export const forceLogout = (data: any) => {
  return createAxios({
    url: '/monitor/online/forceLogout',
    method: 'DELETE',
    data,
  })
}
