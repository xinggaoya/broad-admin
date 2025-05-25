declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    vAuth: string[]
  }
}

declare module '@vue/runtime-dom' {
  interface HTMLElement {
    vAuth?: string[]
  }
}

export {}
