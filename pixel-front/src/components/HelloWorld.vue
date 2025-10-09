<template>
  <div class="hello-world">
    <n-card :title="title">
      <n-space vertical>
        <n-text>{{ message }}</n-text>
        <n-button type="primary" @click="handleClick">Click Me</n-button>
      </n-space>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { useMessage } from 'naive-ui'

interface Props {
  title?: string
  message?: string
}

const props = withDefaults(defineProps<Props>(), {
  title: 'Hello World',
  message: 'Welcome to your Vue 3 + TypeScript + Vite App!'
})

const naiveMessage = useMessage()

const handleClick = () => {
  naiveMessage.info('Button clicked!')
}
</script>

<style scoped>
.hello-world {
  padding: 20px;
}
</style>
