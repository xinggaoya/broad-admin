import { request } from '@/api/request'

export const getUserLogPage = (data: any) => {
  return request.get({
    url: '/sysAdminLog',
    data,
  })
}

export const exportForm = (data: any, filename: string) => {
  return request.download({
    url: '/sysAdminLog/export',
    data,
    filename
  })
}
