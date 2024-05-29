import { request } from '@/api/request'

export const getDict = (data: any) => {
  return request.get({
    url: '/sysDictData',
    data,
  })
}
export const getDictList = (data: any) => {
  return request.get({
    url: '/sysDictData/getDictDataByType',
    data,
  })
}

export const addDict = (data: any) => {
  return request.post({
    url: '/sysDictData',
    data,
  })
}
export const updateDict = (data: any) => {
  return request.put({
    url: '/sysDictData',
    data,
  })
}

export const detectDict = (data: any) => {
  return request.delete({
    url: '/sysDictData',
    data,
  })
}
