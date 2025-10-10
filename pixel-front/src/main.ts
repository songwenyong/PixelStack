import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import i18n from './i18n'

// Naive UI
import naive from 'naive-ui'

// Import styles
import './assets/styles/main.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(naive)
app.use(i18n)

app.mount('#app')
