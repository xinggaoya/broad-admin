import { request } from '@/api/request'

/**
 * 获取系统信息（服务器、CPU、内存、磁盘、JVM）
 */
export const getSystemInfo = () => {
  return request.get({
    url: 'monitor/system'
  })
}

/**
 * 获取服务状态信息
 */
export const getServiceStatus = () => {
  return request.get({
    url: 'monitor/service'
  })
}
