import instance from './kyInstance'
import { createDiscreteApi } from 'naive-ui'
import { useUserStoreHook } from '@/store/modules/user'

// 类型定义
interface KyConfig {
  url: string;
  data?: Record<string, any>;
  filename?: string; // 新增的可选字段，用于指定下载文件名
}

export interface ResModel {
  code: number;
  data?: any;
  rows?: any[];
  total?: number;
  message: string;
}

const { message } = createDiscreteApi(['message'])

export const request = {
  async get(config: KyConfig) {
    config.url = trimSlash(config.url)
    const res = await instance.get(config.url, {
      searchParams: config.data
    }).json() as ResModel
    verificationError(res)
    return res
  },
  async post(config: KyConfig) {
    config.url = trimSlash(config.url)
    const res = await instance.post(config.url, {
      json: config.data
    }).json() as ResModel
    verificationError(res)
    return res
  },
  async put(config: KyConfig) {
    config.url = trimSlash(config.url)
    const res = await instance.put(config.url, {
      json: config.data
    }).json() as ResModel
    verificationError(res)
    return res
  },
  async delete(config: KyConfig) {
    config.url = trimSlash(config.url)
    const res = await instance.delete(config.url, {
      searchParams: config.data
    }).json() as ResModel
    verificationError(res)
    return res
  },
  async download(config: KyConfig) {
    try {
      config.url = trimSlash(config.url)
      const blob = await instance.post(config.url, {
        json: config.data
      }).blob()

      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = config.filename || 'file.xlsx' // 动态文件名
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a) // 确保从 DOM 中移除
      window.URL.revokeObjectURL(url)
    } catch (error) {
      message.error('文件下载失败，请稍后重试')
      console.error('Download error:', error)
      throw error
    }
  }
}

const verificationError = (res: ResModel) => {
  if (res.code === 500 || res.code === 403) {
    message.error(res.message)
    throw new Error(res.message)
  }
  if (res.code === 401) {
    useUserStoreHook().clearUserInfo()
    message.error(res.message)
    throw new Error(res.message)
  }
}

// 判断如果首字符是/，则去掉
const trimSlash = (url: string) => {
  if (url.startsWith('/')) {
    return url.substring(1)
  }
  return url
}
