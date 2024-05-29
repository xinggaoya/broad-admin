import { request } from '@/api/request'

export const getDeptPage = (data: any) => {
  return request.get({
    url: '/sysDept',
    data,
  })
}
export const addDept = (data: any) => {
  return request.post({
    url: '/sysDept',
    data,
  })
}
export const updateDept = (data: any) => {
  return request.put({
    url: '/sysDept',
    data,
  })
}
export const deleteDept = (data: any) => {
  return request.delete({
    url: '/sysDept',
    data,
  })
}
