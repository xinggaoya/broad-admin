import ky, { type Options } from 'ky'
import { useUserStoreHook } from '@/store/modules/user'
import { createDiscreteApi } from 'naive-ui'

const baseURL = import.meta.env.VITE_BASE_AXIOS_URL as string

const { message } = createDiscreteApi(['message'])

const beforeRequest = (request: Request) => {
  request.headers.set('Authorization', useUserStoreHook().token)
  return request
}

const afterResponse = async (request: Request, _options: Options, response: Response) => {
  if (!response.ok) {
    if (response.status === 401) {
      message.error('未认证或登录已过期')
      throw new Error('未认证')
    }
    if (response.status === 403) {
      message.error('无权限访问')
      throw new Error('无权限')
    }
    if (response.status === 500) {
      message.error('服务器异常，请稍后重试…')
      throw new Error('服务器错误')
    }
    message.error('请求失败')
    throw new Error('请求失败')
  }
  return response
}

const defaultConfig: Options = {
  prefixUrl: baseURL,
  timeout: 10000,
  retry: {
    limit: 0
  },
  hooks: {
    beforeRequest: [
      beforeRequest
    ],
    afterResponse: [
      afterResponse
    ]
  },
  headers: {
    'Content-Type': 'application/json'
  }
}

const instance = ky.create(defaultConfig)

export default instance
