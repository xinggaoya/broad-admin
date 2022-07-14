import createAxios from '/@/utils/axios'
import {getAdminToken} from '/@/utils/common'

export function index() {
    return createAxios({
        url: 'sysMenuRule',
        method: 'GET',
    })
}

export function getCaptchaImage() {
    return createAxios({
        url: '/captchaImage',
        method: 'GET',
    })
}

export function login(params:object) {
    return createAxios({
        url: '/sysAdmin/login',
        data: params,
        method: 'POST',
    })
}

export function logout() {
    return createAxios({
        url: '/sysAdmin/logout',
        method: 'POST',
        data: {
            refresh_token: getAdminToken('refresh'),
        },
    })
}
