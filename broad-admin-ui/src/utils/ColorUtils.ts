interface HSL {
  h: number
  s: number
  l: number
}

export function hexToHSL(hex: string): HSL {
  let r = parseInt(hex.slice(1, 3), 16)
  let g = parseInt(hex.slice(3, 5), 16)
  let b = parseInt(hex.slice(5, 7), 16)

  r /= 255
  g /= 255
  b /= 255

  const max = Math.max(r, g, b)
  const min = Math.min(r, g, b)
  let h: number = 0,
    s: number
  const l = (max + min) / 2

  if (max == min) {
    h = s = 0
  } else {
    const d = max - min
    s = l > 0.5 ? d / (2 - max - min) : d / (max + min)
    switch (max) {
      case r:
        h = (g - b) / d + (g < b ? 6 : 0)
        break
      case g:
        h = (b - r) / d + 2
        break
      case b:
        h = (r - g) / d + 4
        break
    }
    h /= 6
  }

  return {
    h: h * 360,
    s: s * 100,
    l: l * 100
  }
}

export function HSLToHex(h: number, s: number, l: number): string {
  s /= 100
  l /= 100

  const c = (1 - Math.abs(2 * l - 1)) * s
  const x = c * (1 - Math.abs(((h / 60) % 2) - 1))
  const m = l - c / 2
  let r = 0,
    g = 0,
    b = 0

  if (0 <= h && h < 60) {
    r = c
    g = x
    b = 0
  } else if (60 <= h && h < 120) {
    r = x
    g = c
    b = 0
  } else if (120 <= h && h < 180) {
    r = 0
    g = c
    b = x
  } else if (180 <= h && h < 240) {
    r = 0
    g = x
    b = c
  } else if (240 <= h && h < 300) {
    r = x
    g = 0
    b = c
  } else if (300 <= h && h < 360) {
    r = c
    g = 0
    b = x
  }

  const rl = Math.round((r + m) * 255)
    .toString(16)
    .padStart(2, '0')
  const gl = Math.round((g + m) * 255)
    .toString(16)
    .padStart(2, '0')
  const bl = Math.round((b + m) * 255)
    .toString(16)
    .padStart(2, '0')

  return `#${rl}${gl}${bl}`
}

// 将十六进制颜色转换为RGB
function hexToRgb(hex: string): number[] | null {
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex)
  return result ? [parseInt(result[1], 16), parseInt(result[2], 16), parseInt(result[3], 16)] : null
}

// 将RGB转换为十六进制颜色
function rgbToHex(r: number, g: number, b: number): string {
  return (
    '#' +
    [r, g, b]
      .map((x) => {
        const hex = x.toString(16)
        return hex.length === 1 ? '0' + hex : hex
      })
      .join('')
  )
}

// 计算hover和active颜色
export function calculateHoverActiveColors(baseColor: string) {
  const rgb = hexToRgb(baseColor)
  if (!rgb) {
    return {
      baseColor,
      hoverColor: baseColor,
      activeColor: baseColor
    }
  }

  const [r, g, b] = rgb

  // hover颜色：变亮
  const hoverR = Math.min(255, r + 25)
  const hoverG = Math.min(255, g + 25)
  const hoverB = Math.min(255, b + 25)

  // active颜色：变暗
  const activeR = Math.max(0, r - 25)
  const activeG = Math.max(0, g - 25)
  const activeB = Math.max(0, b - 25)

  return {
    baseColor,
    hoverColor: rgbToHex(hoverR, hoverG, hoverB),
    activeColor: rgbToHex(activeR, activeG, activeB)
  }
}
