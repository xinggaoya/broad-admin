import { request } from '@/api/request'

export const getJobList = (data: any) => {
  return request.get({
    url: '/job/list',
    data,
  })
}

export const getJobRun = (data: any) => {
  return request.put({
    url: '/job/run',
    data,
  })
}
export const addJob = (data: any) => {
  return request.post({
    url: '/job',
    data,
  })
}
export const jobUpdate = (data: any) => {
  return request.put({
    url: '/job',
    data,
  })
}
export const jobDelete = (data: any) => {
  return request.delete({
    url: '/job/' + data,
  })
}
