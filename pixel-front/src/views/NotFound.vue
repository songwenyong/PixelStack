<template>
  <div class="not-found-container">
    <n-result status="404" :title="$t('notFound.title')" :description="$t('notFound.description')">
      <template #footer>
        <n-button type="primary" @click="goHome">{{ $t('notFound.backHome') }}</n-button>
      </template>
    </n-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

const { t: $t } = useI18n()
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
