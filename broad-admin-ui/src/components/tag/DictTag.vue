<template>
  <div>
    <template v-for="(item, index) in options" :key="index">
      <template v-if="values.includes(item.value)">
        <span
          v-if="item.elTagType === 'default' || item.elTagType === ''"
          :key="item.value"
          :index="index"
          :class="item.elTagClass"
        >{{ item.label }}</span
        >
        <n-tag
          v-else
          :disable-transitions="true"
          :key="item.value + ''"
          :index="index"
          :type="item.elTagType === 'primary' ? '' : item.elTagType"
          :class="item.elTagClass"
        >{{ item.label }}
        </n-tag>
      </template>
    </template>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  options: {
    type: Array,
    default: []
  },
  value: [Number, String, Array]
})

const values = computed(() => {
  if (props.value !== null && typeof props.value !== 'undefined') {
    return Array.isArray(props.value) ? props.value : [String(props.value)]
  } else {
    return []
  }
})
</script>

<style scoped>
.n-tag + .n-tag {
  margin-left: 10px;
}
</style>
