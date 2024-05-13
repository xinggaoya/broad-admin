import { createAxios } from '@/api/request'

export const getLoginLogPage = (data: any) => {
  return createAxios({
    url: '/sysLoginLog',
    method: 'GET',
    data,
  })
}
