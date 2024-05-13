<template>
  <div class="wating-item-action">
    <n-space>
      <n-avatar round>{{ firstChar }}</n-avatar>
      <n-flex vertical>
        <n-space>
          <div class="flex-1 title">{{ item?.name }}</div>
          <div class="item-time flex-1">{{ item?.time }}</div>
        </n-space>
        <div class="flex-1 item-content">{{ item?.content }}</div>
      </n-flex>
    </n-space>
  </div>
</template>

<script lang="ts" setup>
const firstChar = defineModel('item')
</script>

<style lang="scss" scoped>
.wating-item-action {
  padding: 5px;
  margin-bottom: 5px;

  .flex-1 {
    flex: 1;
  }

  .title {
    color: var(--text-color);
    font-weight: bold;
    font-size: 14px;
  }

  .item-time {
    color: #666;
    margin-left: 10px;
  }

  .item-content {
    color: #707070;
    line-height: 18px;
  }
}
</style>
