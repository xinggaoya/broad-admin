import { request } from '@/api/request'

export const getOnline = (data: any) => {
  return request.get({
    url: '/monitor/online',
    data,
  })
}

export const forceLogout = (data: any) => {
  return request.delete({
    url: '/monitor/online/forceLogout',
    data,
  })
}
