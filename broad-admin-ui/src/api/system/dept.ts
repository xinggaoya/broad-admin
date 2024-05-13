import { createAxios } from '@/api/request'

export const getDeptPage = (data: any) => {
  return createAxios({
    url: '/sysDept',
    method: 'GET',
    data,
  })
}
export const addDept = (data: any) => {
  return createAxios({
    url: '/sysDept',
    method: 'POST',
    data,
  })
}
export const updateDept = (data: any) => {
  return createAxios({
    url: '/sysDept',
    method: 'PUT',
    data,
  })
}
export const deleteDept = (data: any) => {
  return createAxios({
    url: '/sysDept',
    method: 'DELETE',
    data,
  })
}
