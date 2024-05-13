import { createAxios } from '@/api/request'

export const getJobList = (data: any) => {
  return createAxios({
    url: '/job/list',
    method: 'GET',
    data,
  })
}

export const getJobRun = (data: any) => {
  return createAxios({
    url: '/job/run',
    method: 'PUT',
    data,
  })
}
export const addJob = (data: any) => {
  return createAxios({
    url: '/job',
    method: 'POST',
    data,
  })
}
export const jobUpdate = (data: any) => {
  return createAxios({
    url: '/job',
    method: 'PUT',
    data,
  })
}
export const jobDelete = (data: any) => {
  return createAxios({
    url: '/job/' + data,
    method: 'Delete',
  })
}
