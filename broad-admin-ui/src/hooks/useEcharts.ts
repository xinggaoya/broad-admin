import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

// 存储所有图表实例
const instances = new Map<HTMLElement, echarts.ECharts>()

// 清理已经不在文档中的实例
function cleanupInstances() {
  for (const [dom, instance] of instances.entries()) {
    if (!document.contains(dom)) {
      try {
        instance.dispose()
      } catch (error) {
        console.warn('Failed to dispose chart instance', error)
      }
      instances.delete(dom)
    }
  }
}

export default function useEcharts(dom: HTMLElement | null, theme?: string) {
  if (!dom) {
    console.warn('useEcharts: DOM element is null')
    return {
      setOption: () => {},
      resize: () => {},
      dispose: () => {},
      getInstance: () => null
    }
  }

  // 定期清理无效实例
  cleanupInstances()

  try {
    // 如果已经存在实例，直接返回
    let instance = instances.get(dom)

    if (!instance) {
      // 检查DOM元素是否仍然在文档中
      if (!document.contains(dom)) {
        console.warn('useEcharts: DOM element is not in document')
        return {
          setOption: () => {},
          resize: () => {},
          dispose: () => {},
          getInstance: () => null
        }
      }

      // 如果不存在实例，创建新的实例
      try {
        // 确保DOM元素有有效的宽高
        const { width, height } = dom.getBoundingClientRect()
        if (width === 0 || height === 0) {
          console.warn('useEcharts: DOM element has zero width or height')
          return {
            setOption: () => {},
            resize: () => {},
            dispose: () => {},
            getInstance: () => null
          }
        }

        instance = echarts.init(dom, theme)
        instances.set(dom, instance)

        // 添加自动调整大小的监听器
        const resizeObserver = new ResizeObserver(() => {
          if (instance && document.contains(dom)) {
            instance.resize()
          }
        })
        resizeObserver.observe(dom)

        // 在实例上存储ResizeObserver以便后续清理
        ;(instance as any).__resizeObserver = resizeObserver
      } catch (error) {
        console.error('useEcharts: Failed to initialize chart', error)
        return {
          setOption: () => {},
          resize: () => {},
          dispose: () => {},
          getInstance: () => null
        }
      }
    }

    return {
      setOption: (option: EChartsOption, opts?: echarts.SetOptionOpts) => {
        try {
          if (instance && document.contains(dom)) {
            instance.setOption(option, opts)
          }
        } catch (error) {
          console.error('useEcharts: Failed to set option', error)
        }
      },
      resize: () => {
        try {
          if (instance && document.contains(dom)) {
            instance.resize()
          }
        } catch (error) {
          console.error('useEcharts: Failed to resize', error)
        }
      },
      dispose: () => {
        try {
          if (instance) {
            // 清理ResizeObserver
            const resizeObserver = (instance as any).__resizeObserver
            if (resizeObserver) {
              resizeObserver.disconnect()
            }
            // 先从Map中移除实例
            instances.delete(dom)
            // 再销毁实例
            instance.dispose()
          }
        } catch (error) {
          console.error('useEcharts: Failed to dispose', error)
        }
      },
      getInstance: () => instance
    }
  } catch (error) {
    console.error('useEcharts: Unexpected error', error)
    return {
      setOption: () => {},
      resize: () => {},
      dispose: () => {},
      getInstance: () => null
    }
  }
}
