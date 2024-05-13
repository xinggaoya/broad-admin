import { createAxios, download } from '@/api/request'

export const getUserLogPage = (data: any) => {
  return createAxios({
    url: '/sysAdminLog',
    method: 'GET',
    data,
  })
}

export const exportForm = (data: any) => {
  return download('/sysAdminLog/export', data, '操作日志.xlsx')
}
