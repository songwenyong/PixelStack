import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

export const useAppStore = defineStore('app', () => {
  // State
  const isDark = ref(false)
  const isCollapsed = ref(false)
  const locale = ref('zh') // Default to Chinese

  // Actions
  const toggleTheme = () => {
    isDark.value = !isDark.value
  }

  const toggleCollapse = () => {
    isCollapsed.value = !isCollapsed.value
  }

  const setLocale = (newLocale: string) => {
    locale.value = newLocale
    // Save to localStorage
    localStorage.setItem('pixelstack-locale', newLocale)
  }

  const initLocale = () => {
    const savedLocale = localStorage.getItem('pixelstack-locale')
    if (savedLocale) {
      locale.value = savedLocale
    }
  }

  return {
    isDark,
    isCollapsed,
    locale,
    toggleTheme,
    toggleCollapse,
    setLocale,
    initLocale
  }
})
