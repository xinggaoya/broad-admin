import type { AxiosRequestConfig, AxiosResponse } from 'axios'
import Axios from 'axios'
import { useUserStoreHook } from '@/store/modules/user'
import { createDiscreteApi } from 'naive-ui'
import { blobValidate } from '@/utils'
// @ts-ignore
import { saveAs } from 'file-saver'

export const baseURL = import.meta.env.VITE_BASE_AXIOS_URL as string

const REQUEST_HEADER = import.meta.env.VITE_BASE_REQUEST_HEADER as string

export const CONTENT_TYPE = 'Content-Type'

export const FORM_URLENCODED = 'application/x-www-form-urlencoded; charset=utf-8'

export const APPLICATION_JSON = 'application/json; charset=utf-8'

export const TEXT_PLAIN = 'text/plain; charset=utf-8'

const service = Axios.create({
  baseURL,
  timeout: 10 * 60 * 1000
})
service.defaults.headers[CONTENT_TYPE] = APPLICATION_JSON
const { message } = createDiscreteApi(['message'])

service.interceptors.request.use(
  (config) => {
    // 添加token请求头
    // @ts-ignore
    if (!config.headers[REQUEST_HEADER]) {
      // @ts-ignore
      config.headers[REQUEST_HEADER] = useUserStoreHook().token
    }
    // 如果get请求，参数为对象，转换为字符串
    if (
      config.method === 'get' ||
      config.method === 'GET' ||
      config.method === 'delete' ||
      (config.method === 'DELETE' && config.data)
    ) {
      config.params = config.data
      config.data = undefined
    }
    return config
  },
  (error) => {
    return Promise.reject(error.response)
  }
)

service.interceptors.response.use(
  (res: AxiosResponse): AxiosResponse => {
    const data = res.data
    // 二进制数据则直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
      return data
    }
    abnormalInspection(data)
    return data
  },
  (error) => {
    if (import.meta.env.MODE === 'development') {
      console.log(error)
    }
    message.error('服务器异常，请稍后重试…')
    return Promise.reject({ code: 500, msg: '服务器异常，请稍后重试…' })
  }
)
/**
 * 异常检测
 * @param res
 */
const abnormalInspection = (res: Response) => {
  if (res.code === 500 || res.code === 403) {
    message.error(res.message)
    throw new Error(res.message)
  }
  if (res.code === 401) {
    useUserStoreHook().clearUserInfo()
    message.error(res.message)
  }
}

export const createAxios = (axiosConfig: AxiosRequestConfig) => {
  return service(axiosConfig)
}

export const download = (
  url: string,
  params: any,
  filename: string,
  config?: AxiosRequestConfig
) => {
  return Axios(url, {
    baseURL,
    method: 'POST',
    data: params,
    headers: {
      [CONTENT_TYPE]: APPLICATION_JSON,
      [REQUEST_HEADER]: useUserStoreHook().token
    },
    responseType: 'blob',
    ...config
  })
    .then(async (data) => {
      const res = data.data
      const isLogin = await blobValidate(res)
      if (isLogin) {
        const blob = new Blob([res])
        saveAs(blob, filename)
      } else {
        const resText = await res.text()
      }
    })
    .catch((r) => {
      console.error(r)
    })
}

export interface Response<T = any> {
  total?: number | 0
  code: number
  message: string
  data?: T
  rows?: Array<T>
}

export default service
