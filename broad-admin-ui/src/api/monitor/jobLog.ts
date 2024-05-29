import { request } from '@/api/request'

export const getJobLogPage = (data: any) => {
  return request.get({
    url: '/job/log/list',
    data,
  })
}
