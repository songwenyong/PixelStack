<template>
  <n-config-provider :theme="theme">
    <n-message-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <router-view />
        </n-notification-provider>
      </n-dialog-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import { computed, onMounted, watch } from 'vue'
import { darkTheme } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'

const { locale } = useI18n()
const appStore = useAppStore()
const userStore = useUserStore()

const theme = computed(() => (appStore.isDark ? darkTheme : null))

// Watch for locale changes in store and sync with i18n
watch(() => appStore.locale, (newLocale) => {
  locale.value = newLocale
}, { immediate: true })

// Initialize app data from localStorage on app mount
onMounted(() => {
  appStore.initLocale()
  userStore.initUserData()
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  width: 100%;
  min-height: 100vh;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
