<template>
  <div class="main-container">
    <div v-if="tabsValue">
      <DicType @update-value="updateValue" />
    </div>
    <div v-else>
      <DicData :dict-type="dictType" @update-value="updateValue" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import DicType from './dictType.vue'
import DicData from './dictData.vue'

const tabsValue = ref(true)
const dictType = ref('')

const updateValue = (value: string) => {
  dictType.value = value
  tabsValue.value = !tabsValue.value
}
</script>
