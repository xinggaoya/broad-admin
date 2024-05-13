import { createAxios } from '@/api/request'

/**
 * 登录
 * @param data
 */
export function login(data: any) {
  return createAxios({
    url: 'sysAdmin/login',
    method: 'POST',
    data: data,
  })
}

/**
 * 主动验证用户
 */
export function checkUser() {
  return createAxios({
    url: 'sysAdmin/checkLogin',
    method: 'GET',
  })
}

/**
 * 退出登录
 */
export function logoutAdmin() {
  return createAxios({
    url: 'sysAdmin/logout',
    method: 'GET',
  })
}

/**
 * 获取菜单列表
 * @param data
 */
export function getRoutes() {
  return createAxios({
    url: '/sysMenu/getRouters',
    method: 'GET',
  })
}

/**
 * 获取验证码
 * @param data
 */
export function getCaptchaImage() {
  return createAxios({
    url: '/captchaImage',
    method: 'GET',
  })
}
