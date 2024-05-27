<template>
  <div id="richTextEditor" :style="{ height: height + 'px' }"></div>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue'
import Quill from 'quill'
import 'quill/dist/quill.snow.css'

defineProps({
  height: {
    type: [Number, String],
    default: 'auto'
  }
})

let quill: Quill | null = null
const init = () => {
  const options = {
    modules: {
      toolbar: [
        ['bold', 'italic', 'underline', 'strike'], // 切换按钮
        ['blockquote', 'code-block'],

        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ script: 'sub' }, { script: 'super' }], // 上标/下标
        [{ indent: '-1' }, { indent: '+1' }], // 减少缩进/缩进
        [{ direction: 'rtl' }], // 文本下划线

        [{ size: ['small', false, 'large', 'huge'] }], // 用户自定义下拉
        [{ header: [1, 2, 3, 4, 5, 6, false] }],

        [{ color: [] }, { background: [] }], // 主题默认下拉，使用主题提供的值
        [{ align: [] }],

        ['link', 'image'],

        ['clean'] // 清除格式
      ]
    },
    placeholder: '请输入文章内容…',
    theme: 'snow'
  }
  quill = new Quill(document.getElementById('richTextEditor') as Element, options)
}
onMounted(init)

const getHtmlContent = () => {
  return (document.getElementById('richTextEditor')?.firstChild as any).innerHTML
}
const getJsonContent = () => {
  return JSON.stringify(quill?.getContents())
}

const setContents = (content: any) => {
  quill?.setContents(content)
}

</script>
