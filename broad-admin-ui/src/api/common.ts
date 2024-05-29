import { request } from '@/api/request'

/**
 * 登录
 * @param data
 */
export function login(data: any) {
  return request.post({
    url: 'sysAdmin/login',
    data: data,
  })
}

/**
 * 主动验证用户
 */
export function checkUser() {
  return request.post({
    url: 'sysAdmin/checkLogin',
  })
}

/**
 * 退出登录
 */
export function logoutAdmin() {
  return request.get({
    url: 'sysAdmin/logout',
  })
}

/**
 * 获取菜单列表
 * @param data
 */
export function getRoutes() {
  return request.get({
    url: '/sysMenu/getRouters'
  })
}

/**
 * 获取验证码
 * @param data
 */
export function getCaptchaImage() {
  return request.get({
    url: '/captchaImage',
  })
}
