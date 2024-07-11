import usePermissionStoreHook from '@/store/modules/permission'
import {h, reactive} from 'vue'
import {NButton} from 'naive-ui'
import type {TableActionModel} from '@/types/table'
import DictTag from '@/components/tag/DictTag.vue'

export const useRenderAction = function (actions: TableActionModel[]) {
    const permissionStore = usePermissionStoreHook()
    return actions.map((it) => {
        // 权限控制 是否显示
        if (!it.auth || (permissionStore.hasPermissionExists(it.auth) && !it.hidden)) {
            return h(
                NButton,
                {
                    type: it.type || 'primary',
                    size: 'small',
                    ghost: true,
                    disabled: it.disabled || false,
                    onClick: it.onClick
                },
                {
                    default: () => it.label
                }
            )
        }
    })
}
/**
 *  渲染字典标签
 * @param options
 * @param value
 */
export const useRenderTag = function (options: object, value: any) {
    return h(DictTag, {
        options: options,
        value: value
    })
}

/**
 * @description: 表格分页 入参为数据刷新方法
 * @param callback
 */
export const usePagination = function (callback: () => void) {
    function onChange(page: number) {
        paginationInfo.page = page
        callback()
    }

    function onPageSizeChange(pageSize: number) {
        paginationInfo.pageSize = pageSize
        paginationInfo.page = 1
        callback()
    }

    function getPageInfo(searchParams?: any) {
        const parameters: any = {
            pageNum: paginationInfo.page,
            pageSize: paginationInfo.pageSize
        }
        if (searchParams) {
            // 筛选值不为空的属性
            Object.keys(searchParams).forEach((key) => {
                if (searchParams[key]) {
                    parameters[key] = searchParams[key]
                }
            })
        }
        return parameters
    }

    const paginationInfo = reactive({
        page: 1,
        pageSize: 10,
        showSizePicker: true,
        showQuickJumper: true,
        pageCount: 1,
        pageSizes: [10, 20, 30, 40],
        onChange,
        onPageSizeChange,
        getPageInfo,
        setTotalSize(totalSize: number) {
            paginationInfo.pageCount = Math.ceil(totalSize / paginationInfo.pageSize)
        }
    })
    return paginationInfo
}

export function renderDictTag(options: Array<any>, value: any) {
    return h(DictTag, {
        options: options,
        value: value
    })
}
