import createAxios from '/@/utils/axios'

export function getMenuRules(params: any) {
    return createAxios({
        url: 'sysAdminGroup',
        method: 'get',
        params: params
    })
}
