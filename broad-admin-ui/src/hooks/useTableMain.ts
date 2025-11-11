import { ref } from 'vue'
import type { TableMainExpose } from '@/types/table'

/**
 * TableMain 统一操作 hook，便于在页面中快速调用刷新、重置、全屏等方法
 */
export const useTableMain = () => {
  const tableMainRef = ref<TableMainExpose | null>(null)

  const refresh = (params?: Record<string, any>) => {
    tableMainRef.value?.search(params)
  }

  const reset = () => {
    tableMainRef.value?.resetSearch()
  }

  const toggleFullscreen = () => {
    tableMainRef.value?.toggleFullscreen()
  }

  const getSearchData = () => {
    return tableMainRef.value?.getSearchData() || {}
  }

  return {
    tableMainRef,
    refresh,
    reset,
    toggleFullscreen,
    getSearchData
  }
}
