<template>
  <div class="not-found-container">
    <n-result status="404" title="404 Not Found" description="The page you are looking for does not exist.">
      <template #footer>
        <n-button type="primary" @click="goHome">Back to Home</n-button>
      </template>
    </n-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 24px;
}
</style>
