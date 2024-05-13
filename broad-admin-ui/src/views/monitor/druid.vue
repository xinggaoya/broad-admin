<template>
  <Iframe :src="url" />
</template>

<script lang="ts" setup>
  import { ref } from 'vue'

  const url = ref(import.meta.env.VITE_BASE_AXIOS_URL + '/druid/login.html')
</script>
