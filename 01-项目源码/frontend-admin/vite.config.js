import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 4010,
    proxy: {
      '/api': {
        target: 'http://localhost:2010',
        changeOrigin: true
      },
      '/resources/uploads': {
        target: 'http://localhost:2010',
        changeOrigin: true
      }
    }
  }
})
