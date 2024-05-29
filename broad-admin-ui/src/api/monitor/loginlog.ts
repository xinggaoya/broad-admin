import { request } from '@/api/request'

export const getLoginLogPage = (data: any) => {
  return request.get({
    url: '/sysLoginLog',
    data,
  })
}
