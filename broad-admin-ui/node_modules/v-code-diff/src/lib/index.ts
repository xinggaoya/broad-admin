import { Plugin } from 'vue-demi'
import CodeDiff from './v-code-diff'

const components = [
  CodeDiff
]
const install = (app) => {
  components.forEach(component => {
    app.component(component.name, component)
  })
}

export {
  install,
  CodeDiff
}

export default { install } as Plugin
