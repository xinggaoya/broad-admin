<template>
  <component :is="component" :class="className" aria-hidden="true">
    <use :href="iconName" />
  </component>
</template>

<script lang="ts" setup>
import { computed } from 'vue'

defineOptions({
  name: 'SvgIcon'
})
const props = defineProps({
  prefix: {
    type: String,
    default: 'icon'
  },
  name: {
    type: String,
    required: true
  }
})

const component = computed(() => {
  return props.prefix === 'icon' ? 'svg' : 'i'
})

const iconName = computed(() => {
  return `#${props.prefix}-${props.name}`
})
const className = computed(() => {
  if (props.prefix === 'icon') {
    return 'svg-icon'
  } else if (props.prefix === 'iconfont') {
    return 'iconfont icon-' + props.name
  } else {
    return ''
  }
})
</script>

<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.svg-icon:hover {
  fill: var(--primary-color-hover);
}

.svg-external-icon {
  background-color: currentColor;
  mask-size: cover !important;
  display: inline-block;
}
</style>
