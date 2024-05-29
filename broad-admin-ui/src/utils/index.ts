import type { DataTableColumn, TreeSelectOption } from 'naive-ui'
import type { TablePropsType } from '@/types/components'
import type { Ref } from 'vue'
// @ts-ignore
import tinycolor from 'tinycolor2'

export function isExternal(path: string) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

export function uuid() {
  const s: Array<any> = []
  const hexDigits = '0123456789abcdef'
  for (let i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
  }
  s[14] = '4' // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1) // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = '-'
  const uuid = s.join('')
  return uuid
}

export function randomString(length: number) {
  const str = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
  let result = ''
  for (let i = length; i > 0; --i) {
    result += str[Math.floor(Math.random() * str.length)]
  }
  return result
}

/**
 * 中划线字符驼峰
 * @param {*} str 要转换的字符串
 * @returns 返回值
 */
export function toHump(str: string): string {
  if (!str) return str
  return str
    .replace(/\-(\w)/g, function(all, letter) {
      return letter.toUpperCase()
    })
    .replace(/(\s|^)[a-z]/g, function(char) {
      return char.toUpperCase()
    })
}

export function sortColumns(originColumns: DataTableColumn[], newColumns: TablePropsType[]) {
  if (!originColumns || !newColumns) {
    return
  }
  if (newColumns.length === 0) {
    originColumns.length = 0
  } else {
    const selectionItem = originColumns.find((it) => it.type === 'selection')
    originColumns.length = 0
    if (selectionItem) {
      originColumns.push(selectionItem)
    }
    originColumns.push(...newColumns)
  }
}

export function transformTreeSelect(
  origin: any[],
  labelName: string,
  keyName: string
): TreeSelectOption[] {
  const tempSelections: TreeSelectOption[] = []
  origin.forEach((it) => {
    const selection = {
      label: it[labelName],
      key: it[keyName]
    } as TreeSelectOption
    if (it.children) {
      selection.children = transformTreeSelect(it.children, labelName, keyName)
    }
    tempSelections.push(selection)
  })
  return tempSelections
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params: any) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    const part = encodeURIComponent(propName) + '='
    if (value !== null && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && typeof value[key] !== 'undefined') {
            const params = propName + '[' + key + ']'
            const subPart = encodeURIComponent(params) + '='
            result += subPart + encodeURIComponent(value[key]) + '&'
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&'
      }
    }
  }
  return result
}

/**
 * 验证是否为blob格式
 * @param data
 */
export async function blobValidate(data: any) {
  try {
    const text = await data.text()
    JSON.parse(text)
    return false
  } catch (error) {
    return true
  }
}

/**
 * 清空表单对象
 * @param data
 */
export function clearFormObject(data: Ref) {
  const obj = data.value
  Object.keys(obj).forEach((key) => {
    if (obj[key] instanceof Array) {
      obj[key] = []
    } else if (obj[key] instanceof Object) {
      obj[key] = {}
    } else {
      obj[key] = null
    }
  })
}

/**
 * 递归map部门处理
 *
 * @param data
 */
export function mapTree(data: any) {
  return data.map((item: any) => {
    if (item.children && item.children.length > 0) {
      return {
        label: item.deptName,
        value: item.deptId,
        key: item.deptId,
        children: mapTree(item.children)
      }
    } else {
      return {
        label: item.deptName,
        value: item.deptId,
        key: item.deptId
      }
    }
  })
}

// 判断当前是否https 并生成对应wss链接
export function getWebSocketUrl() {
  let url = window.location.host
  if (window.location.protocol === 'http:') {
    url = `ws://${url}`
  } else {
    url = `wss://${url}`
  }
  return url
}
