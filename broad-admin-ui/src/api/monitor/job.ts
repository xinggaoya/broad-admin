import { request } from '@/api/request'

export const getJobList = (data: Record<string, any>) => {
  return request.get({
    url: '/job/list',
    data
  })
}

export const getJobMeta = () => {
  return request.get({
    url: '/job/meta'
  })
}

export const getJobRun = (data: Record<string, any>) => {
  return request.put({
    url: '/job/run',
    data
  })
}

export const addJob = (data: Record<string, any>) => {
  return request.post({
    url: '/job',
    data
  })
}

export const jobUpdate = (data: Record<string, any>) => {
  return request.put({
    url: '/job',
    data
  })
}

export const jobDelete = (jobIds: number | string) => {
  return request.delete({
    url: `/job/${jobIds}`
  })
}

export const changeJobStatus = (data: Record<string, any>) => {
  return request.put({
    url: '/job/changeStatus',
    data
  })
}
