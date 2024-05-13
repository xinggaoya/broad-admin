import { getDictList } from '@/api/system/dict'
import { useDictStore } from '@/store/modules/dict'
import { ref, toRefs } from 'vue'

/**
 * 获取字典数据
 */
export function useDict(...args: string[]) {
  const res = ref<any>({})
  return (() => {
    args.forEach((dictType, index) => {
      res.value[dictType] = []
      const dicts = useDictStore().getDict(dictType)
      if (dicts) {
        res.value[dictType] = dicts
      } else {
        getDictList({ dictType: dictType }).then((resp) => {
          res.value[dictType] = resp.data.map((p: any) => ({
            label: p.dictLabel,
            value: p.dictValue,
            elTagType: p.listClass,
            elTagClass: p.cssClass,
          }))
          useDictStore().setDict(dictType, res.value[dictType])
        })
      }
    })
    return toRefs(res.value)
  })()
}
