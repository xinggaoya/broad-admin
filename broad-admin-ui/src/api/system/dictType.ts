import { request } from '@/api/request'

export const getDictTypePage = (data: any) => {
  return request.get({
    url: '/sysDictType',
    data,
  })
}
export const getDictType = (data: any) => {
  return request.get({
    url: '/sysDictType/list',
    data,
  })
}
export const addDictType = (data: any) => {
  return request.post({
    url: '/sysDictType',
    data,
  })
}
export const updateDictType = (data: any) => {
  return request.put({
    url: '/sysDictType',
    data,
  })
}
export const detectDictType = (id: number) => {
  return request.delete({
    url: '/sysDictType/' + id,
  })
}
