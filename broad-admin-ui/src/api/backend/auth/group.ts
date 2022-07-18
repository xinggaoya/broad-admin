import createAxios from '/@/utils/axios'

export function getMenuRules() {
    return createAxios({
        url: 'sysAdminGroup',
        method: 'get',
    })
}
