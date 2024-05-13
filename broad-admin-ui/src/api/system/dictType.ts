import { createAxios } from '@/api/request'

export const getDictTypePage = (data: any) => {
  return createAxios({
    url: '/sysDictType',
    method: 'GET',
    data,
  })
}
export const getDictType = (data: any) => {
  return createAxios({
    url: '/sysDictType/list',
    method: 'GET',
    data,
  })
}
export const addDictType = (data: any) => {
  return createAxios({
    url: '/sysDictType',
    method: 'POST',
    data,
  })
}
export const updateDictType = (data: any) => {
  return createAxios({
    url: '/sysDictType',
    method: 'PUT',
    data,
  })
}
export const detectDictType = (id: number) => {
  return createAxios({
    url: '/sysDictType/' + id,
    method: 'DELETE',
  })
}
