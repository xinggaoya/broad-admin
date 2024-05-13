import { createAxios } from '@/api/request'

export const getJobLogPage = (data: any) => {
  return createAxios({
    url: '/job/log/list',
    method: 'GET',
    data,
  })
}
