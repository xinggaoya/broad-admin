import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useDictStore = defineStore('dict', () => {
  const dict = ref<Array<any>>([])

  // 获取字典
  function getDict(_key: string) {
    if (_key == null && _key == '') {
      return null
    }
    try {
      for (let i = 0; i < dict.value.length; i++) {
        if (dict.value[i].key == _key) {
          return dict.value[i].value
        }
      }
    } catch (e) {
      return null
    }
  }

  // 设置字典
  function setDict(_key: string, value: string) {
    if (_key !== null && _key !== '') {
      dict.value.push({
        key: _key,
        value: value,
      })
    }
  }

  // 删除字典
  function removeDict(_key: string) {
    let bln = false
    try {
      for (let i = 0; i < dict.value.length; i++) {
        if (dict.value[i].key == _key) {
          dict.value.splice(i, 1)
          return true
        }
      }
    } catch (e) {
      bln = false
    }
    return bln
  }

  // 清空字典
  function cleanDict() {
    dict.value = []
  }

  // 初始字典
  function initDict() {}

  return {
    dict,
    getDict,
    setDict,
    removeDict,
    cleanDict,
    initDict,
  }
})
