import { createPatch } from 'diff'
import * as d2h from 'diff2html'
import hljs from 'highlight.js'
import { SetupContext } from 'vue-demi'

type Props = Readonly<{
  highlight: boolean
  oldString: string
  newString: string
  context: number
  outputFormat: 'side-by-side' | 'line-by-line'
  drawFileList: boolean
  renderNothingWhenEmpty: boolean
  fileName: string
  isShowNoChange: boolean
  diffStyle: 'word' | 'char'
  trim: boolean
  language: string
  noDiffLineFeed: boolean
} & {}>

export function useDebounceFn (fn, delay) {
  let timer
  return function () {
    const context = this
    const args = arguments
    clearTimeout(timer)
    timer = setTimeout(function () {
      fn.apply(context, args)
    }, delay)
  }
}

export const createHtml = (props: Props) => {
  let oldString = props.trim ? props.oldString.trim() : props.oldString
  let newString = props.trim ? props.newString.trim() : props.newString
  oldString = props.noDiffLineFeed ? oldString.replace(/(\r\n)/g, '\n') : oldString
  newString = props.noDiffLineFeed ? newString.replace(/(\r\n)/g, '\n') : newString
  let context = props.context
  if (props.isShowNoChange && oldString === newString) {
    oldString = 'File Without Change\tOldString: ======================== \n' + oldString
    newString = 'File Without Change\tNewString: ======================== \n' + newString
    context = 99999
  }
  const dd = createPatch(props.fileName, oldString, newString, '', '', { context: context })
  return d2h.html(dd, {
    outputFormat: props.outputFormat,
    drawFileList: props.drawFileList,
    matching: 'lines',
    diffStyle: props.diffStyle,
    renderNothingWhenEmpty: props.renderNothingWhenEmpty
  })
}

async function listElements (element: Element): Promise<Element[]> {
  return new Promise(resolve => {
    setTimeout(() => {
      const elements = element.querySelectorAll('.d2h-wrapper .d2h-code-line-ctn')
      resolve(Array.from(elements))
    }, 0)
  })
}

async function highlightElement (el: HTMLElement, language: string): Promise<boolean> {
  return new Promise(resolve => {
    setTimeout(() => {
      if (language) {
        const text = el.innerText
        el.innerHTML = hljs.highlight(text, { language: language }).value
      } else {
        hljs.highlightElement(<HTMLElement>el)
      }
      resolve(true)
    }, 0)
  })
}

export async function highlightElements (element: Element, props, ctx: SetupContext<any>) {
  ctx.emit('before-render')
  const elements = await listElements(element)
  const promises = Array.from(elements).map(el => highlightElement(el as HTMLElement, props.language))
  await Promise.all(promises)
  ctx.emit('after-render')
}

export function useSyncScroll (selector) {
  let active: HTMLElement = document.createElement('div')

  function onScroll (e) {
    if (e.target !== active) return

    document.querySelectorAll(selector).forEach(function (target) {
      if (active === target) return

      target.scrollTop = active.scrollTop
      target.scrollLeft = active.scrollLeft
    })
  }

  function onMouseenter (e) {
    active = e.target
  }

  function addSyncScroll () {
    document.querySelectorAll(selector).forEach(function (element) {
      element.addEventListener('mouseenter', onMouseenter)
      element.addEventListener('scroll', onScroll)
    })
  }

  function removeSyncScroll () {
    document.querySelectorAll(selector).forEach(function (element) {
      element.removeEventListener('mouseenter', onMouseenter)
      element.removeEventListener('scroll', onScroll)
    })
  }

  return {
    addSyncScroll,
    removeSyncScroll
  }
}
