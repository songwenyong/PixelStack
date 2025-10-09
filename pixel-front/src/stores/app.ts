import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // State
  const isDark = ref(false)
  const isCollapsed = ref(false)

  // Actions
  const toggleTheme = () => {
    isDark.value = !isDark.value
  }

  const toggleCollapse = () => {
    isCollapsed.value = !isCollapsed.value
  }

  return {
    isDark,
    isCollapsed,
    toggleTheme,
    toggleCollapse
  }
})
