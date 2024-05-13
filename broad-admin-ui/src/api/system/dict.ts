import { createAxios } from '@/api/request'

export const getDict = (data: any) => {
  return createAxios({
    url: '/sysDictData',
    method: 'GET',
    data,
  })
}
export const getDictList = (data: any) => {
  return createAxios({
    url: '/sysDictData/getDictDataByType',
    method: 'GET',
    data,
  })
}

export const addDict = (data: any) => {
  return createAxios({
    url: '/sysDictData',
    method: 'POST',
    data,
  })
}
export const updateDict = (data: any) => {
  return createAxios({
    url: '/sysDictData',
    method: 'PUT',
    data,
  })
}

export const detectDict = (data: any) => {
  return createAxios({
    url: '/sysDictData',
    method: 'DELETE',
    data,
  })
}
