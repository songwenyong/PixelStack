import { createI18n } from 'vue-i18n'
import zh from './locales/zh'
import en from './locales/en'

export const LOCALE_OPTIONS = [
  { label: '中文', value: 'zh' },
  { label: 'English', value: 'en' }
]

const i18n = createI18n({
  legacy: false,
  locale: 'zh', // Default to Chinese
  fallbackLocale: 'en',
  messages: {
    zh,
    en
  }
})

export default i18n