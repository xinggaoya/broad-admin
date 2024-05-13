<template>
  <n-radio-group :default-value="list[0].value">
    <n-radio-button
      v-for="(song, index) in list"
      :name="song.value"
      :label="song.label"
      :value="song.value"
      :key="index"
    />
  </n-radio-group>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue'

defineProps<{
  list: any[]
}>()
</script>

<style scoped></style>
