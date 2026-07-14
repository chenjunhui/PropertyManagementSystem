import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import i18n from './i18n'
import { useThemeStore } from './stores/theme'
import './styles/global.css'

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)

// Apply saved theme on startup
const themeStore = useThemeStore()
themeStore.setTheme(themeStore.current)

app.use(router).use(i18n).mount('#app')
