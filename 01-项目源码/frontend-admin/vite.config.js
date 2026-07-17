import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  base: '/admin/',
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules/vue') || id.includes('node_modules/pinia') || id.includes('node_modules/vue-router')) {
            return 'vue'
          }
        }
      }
    }
  },
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
