import createAxios from '/@/utils/axios'


export function index() {
    return createAxios({
        url: '/sysMenuRule',
        method: 'get',
    })
}
