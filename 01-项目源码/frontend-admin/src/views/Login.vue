<template>
  <div class="login-page">
    <div class="login-overlay"></div>
    <div class="top-controls">
      <LangSwitcher />
      <ThemeSwitcher />
    </div>
    <div class="login-shell">
      <aside class="login-hero">
        <div class="hero-brand">
          <div class="brand-mark" v-html="brandIconSvg"></div>
          <div>
            <p class="hero-kicker">Property Management</p>
            <h2>{{ t('auth.propertySystem') }}</h2>
          </div>
        </div>
        <p class="hero-desc">{{ t('auth.systemDesc') }}</p>
        <ul class="feature-list">
          <li>{{ t('auth.feature1') }}</li>
          <li>{{ t('auth.feature2') }}</li>
          <li>{{ t('auth.feature3') }}</li>
        </ul>
        <div class="hero-stats">
          <div class="stat-chip"><span class="stat-num">3</span><span class="stat-label">{{ t('auth.statBuildings') }}</span></div>
          <div class="stat-chip"><span class="stat-num">8+</span><span class="stat-label">{{ t('auth.statUnits') }}</span></div>
          <div class="stat-chip"><span class="stat-num">24h</span><span class="stat-label">{{ t('auth.statResponse') }}</span></div>
        </div>
      </aside>
      <div class="login-card">
        <div class="login-header">
          <span class="portal-badge">{{ t('auth.adminPortal') }}</span>
          <h1>{{ t('auth.welcome') }}</h1>
          <p>{{ t('auth.adminDesc') }}</p>
        </div>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label>{{ t('auth.username') }}</label>
            <input v-model="form.username" class="input" :placeholder="t('auth.usernamePlaceholder')" autocomplete="username" required />
          </div>
          <div class="form-group">
            <label>{{ t('auth.password') }}</label>
            <input v-model="form.password" type="password" class="input" :placeholder="t('auth.passwordPlaceholder')" autocomplete="current-password" required />
          </div>
          <p v-if="error" class="error">{{ error }}</p>
          <button class="btn btn-primary login-btn" type="submit" :disabled="loading">
            {{ loading ? t('auth.loginLoading') : t('auth.enterAdmin') }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../stores/user'
import { brandIconSvg } from '../utils/brandIcon'
import LangSwitcher from '../components/LangSwitcher.vue'
import ThemeSwitcher from '../components/ThemeSwitcher.vue'

const { t } = useI18n()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const error = ref('')
const form = reactive({ username: 'admin', password: 'admin123' })

async function handleLogin() {
  loading.value = true
  error.value = ''
  try {
    await userStore.login(form.username, form.password)
    router.push('/dashboard')
  } catch (e) {
    error.value = e.message || t('auth.loginFailed')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  position: relative;
  background: url('/resources/images/login/admin-bg.svg') center/cover no-repeat fixed;
  background-color: var(--color-primary-bg);
}

.login-overlay {
  position: absolute;
  inset: 0;
  background: var(--color-primary-bg);
  opacity: 0.85;
  pointer-events: none;
}

.top-controls {
  position: fixed;
  top: 16px;
  right: 16px;
  z-index: 100;
  display: flex;
  align-items: center;
  gap: 10px;
  background: linear-gradient(135deg, var(--color-primary-dark), var(--color-primary));
  padding: 8px 12px;
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.login-shell {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  display: grid;
  grid-template-columns: minmax(300px, 1fr) min(440px, 100%);
  gap: clamp(24px, 5vw, 64px);
  align-items: center;
  max-width: 1280px;
  margin: 0 auto;
  padding: clamp(24px, 5vw, 64px) clamp(24px, 6vw, 96px);
}

.login-hero {
  color: var(--color-text);
  animation: fadeUp 0.5s ease-out;
}

.hero-brand {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 28px;
}

.brand-mark {
  width: 56px;
  height: 56px;
  flex-shrink: 0;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
  color: #fff;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 12px 32px var(--shadow-color);
}

.hero-kicker {
  font-size: 13px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--color-primary);
  margin-bottom: 6px;
}

.login-hero h2 {
  font-size: clamp(28px, 4vw, 38px);
  font-weight: 700;
  line-height: 1.2;
  letter-spacing: -0.02em;
}

.hero-desc {
  max-width: 520px;
  font-size: 16px;
  line-height: 1.75;
  color: var(--color-text-secondary);
  margin-bottom: 28px;
}

.feature-list {
  list-style: none;
  display: grid;
  gap: 14px;
  margin-bottom: 32px;
}

.feature-list li {
  position: relative;
  padding-left: 22px;
  font-size: 15px;
  color: var(--color-text);
}

.feature-list li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 9px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--color-primary);
  box-shadow: 0 0 0 4px var(--shadow-color);
}

.hero-stats {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
}

.stat-chip {
  padding: 12px 18px;
  background: var(--color-primary-bg);
  border: 1px solid var(--color-primary-border);
  border-radius: 14px;
}

.stat-num {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: var(--color-primary);
  line-height: 1.2;
}

.stat-label {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.login-card {
  width: 100%;
  padding: 40px 36px;
  background: rgba(255, 255, 255, 0.94);
  backdrop-filter: blur(20px) saturate(1.15);
  border: 1px solid rgba(255, 255, 255, 0.88);
  border-radius: 22px;
  box-shadow: 0 24px 64px rgba(6, 78, 59, 0.28), 0 8px 24px rgba(15, 23, 42, 0.08);
  animation: slideIn 0.45s ease-out;
}

@keyframes slideIn {
  from { opacity: 0; transform: translateX(24px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(16px); }
  to { opacity: 1; transform: translateY(0); }
}

.login-header { margin-bottom: 28px; }

.portal-badge {
  display: inline-block;
  padding: 4px 12px;
  margin-bottom: 12px;
  font-size: 12px;
  font-weight: 600;
  color: var(--color-primary-dark);
  background: var(--color-primary-bg);
  border: 1px solid var(--color-primary-border);
  border-radius: 20px;
}

.login-header h1 {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
  color: var(--color-text);
  letter-spacing: -0.02em;
}

.login-header p { font-size: 14px; color: var(--color-text-secondary); }

.form-group { margin-bottom: 18px; }

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 8px;
}

.input {
  background: #fff;
  border-color: var(--color-primary-border);
  padding: 12px 14px;
}

.input:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--shadow-color);
}

.login-btn {
  width: 100%;
  margin-top: 4px;
  padding: 13px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
  border: none;
}

.login-btn:hover:not(:disabled) { filter: brightness(1.05); }

.error {
  color: #dc2626;
  font-size: 13px;
  margin-bottom: 10px;
  padding: 8px 12px;
  background: rgba(220, 38, 38, 0.06);
  border-radius: 8px;
}

@media (max-width: 960px) {
  .login-shell { grid-template-columns: 1fr; max-width: 480px; }
  .login-hero { display: none; }
  .login-overlay {
    background: var(--color-primary-bg);
    opacity: 0.9;
  }
}
</style>
