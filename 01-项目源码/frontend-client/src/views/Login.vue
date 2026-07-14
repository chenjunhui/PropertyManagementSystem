<template>
  <div class="login-page">
    <div class="login-overlay"></div>
    <div class="top-controls">
      <LangSwitcher />
      <ThemeSwitcher />
    </div>
    <div class="login-shell">
      <div class="login-card">
        <div class="login-header">
          <div class="brand-mark" v-html="brandIconSvg"></div>
          <span class="portal-badge">{{ t('auth.clientPortal') }}</span>
          <h1>{{ t('auth.propertySystem') }}</h1>
          <p>{{ t('auth.systemDesc') }}</p>
        </div>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label>{{ t('auth.username') }}</label>
            <input v-model="form.username" class="input" placeholder="owner001" autocomplete="username" required />
          </div>
          <div class="form-group">
            <label>{{ t('auth.password') }}</label>
            <input v-model="form.password" type="password" class="input" :placeholder="t('auth.passwordPlaceholder')" autocomplete="current-password" required />
          </div>
          <p v-if="error" class="error">{{ error }}</p>
          <button class="btn btn-primary" type="submit" :disabled="loading">
            {{ loading ? t('auth.loginLoading') : t('auth.enterClient') }}
          </button>
        </form>
        <p class="register-link">{{ t('auth.noAccount') }}<router-link to="/register">{{ t('auth.register') }}</router-link></p>
      </div>
      <aside class="login-hero">
        <p class="hero-kicker">Smart · Secure · Home</p>
        <h2>{{ t('auth.systemDesc') }}</h2>
        <ul class="feature-list">
          <li>{{ t('auth.feature1') }}</li>
          <li>{{ t('auth.feature2') }}</li>
          <li>{{ t('auth.feature3') }}</li>
        </ul>
      </aside>
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
const form = reactive({ username: 'owner001', password: '123456' })

async function handleLogin() {
  loading.value = true
  error.value = ''
  try {
    await userStore.login(form.username, form.password)
    router.push('/home')
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
  background: url('/resources/images/login/client-bg.svg') center/cover no-repeat fixed;
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
  grid-template-columns: min(440px, 100%) minmax(280px, 1fr);
  gap: clamp(24px, 5vw, 72px);
  align-items: center;
  max-width: 1180px;
  margin: 0 auto;
  padding: clamp(24px, 5vw, 64px) clamp(24px, 6vw, 96px);
}

.login-card {
  width: 100%;
  padding: 38px 34px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(18px) saturate(1.15);
  border: 1px solid rgba(255, 255, 255, 0.95);
  border-radius: 22px;
  box-shadow: 0 20px 56px var(--shadow-color), 0 8px 24px rgba(15, 23, 42, 0.06);
  animation: slideIn 0.45s ease-out;
}

@keyframes slideIn {
  from { opacity: 0; transform: translateX(-20px); }
  to { opacity: 1; transform: translateX(0); }
}

.login-header { margin-bottom: 28px; }

.brand-mark {
  width: 52px;
  height: 52px;
  margin-bottom: 16px;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
  color: #fff;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 28px var(--shadow-color);
}

.portal-badge {
  display: inline-block;
  padding: 4px 12px;
  margin-bottom: 14px;
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
  color: #064e3b;
  margin-bottom: 8px;
  letter-spacing: -0.02em;
}

.login-header p {
  font-size: 14px;
  color: #64748b;
  line-height: 1.6;
}

.form-group { margin-bottom: 18px; }

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 8px;
}

.input {
  background: rgba(255, 255, 255, 0.98);
  border-color: var(--color-primary-border);
  padding: 12px 14px;
}

.input:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--shadow-color);
}

.btn-primary {
  width: 100%;
  margin-top: 4px;
  padding: 13px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
  border: none;
}

.error {
  color: #dc2626;
  font-size: 13px;
  margin-bottom: 10px;
  padding: 8px 12px;
  background: rgba(220, 38, 38, 0.06);
  border-radius: 8px;
}

.register-link {
  text-align: center;
  margin-top: 22px;
  font-size: 14px;
  color: #64748b;
}

.register-link a {
  color: var(--color-primary);
  font-weight: 600;
  text-decoration: none;
}

.register-link a:hover { text-decoration: underline; }

.login-hero {
  color: #064e3b;
  animation: fadeUp 0.55s ease-out 0.08s both;
}

.hero-kicker {
  font-size: 13px;
  letter-spacing: 0.14em;
  text-transform: uppercase;
  color: var(--color-primary);
  margin-bottom: 16px;
  font-weight: 600;
}

.login-hero h2 {
  font-size: clamp(30px, 4vw, 42px);
  font-weight: 800;
  line-height: 1.25;
  margin-bottom: 28px;
  letter-spacing: -0.03em;
}

.feature-list {
  list-style: none;
  display: grid;
  gap: 16px;
}

.feature-list li {
  position: relative;
  padding: 14px 16px 14px 44px;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid var(--color-primary-border);
  border-radius: 14px;
  font-size: 15px;
  color: var(--color-primary-dark);
  box-shadow: 0 8px 24px var(--shadow-color);
}

.feature-list li::before {
  content: '';
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: var(--color-primary);
  box-shadow: 0 0 0 4px var(--shadow-color);
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(18px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 960px) {
  .login-shell {
    grid-template-columns: 1fr;
    max-width: 480px;
  }
  .login-hero { display: none; }
  .login-overlay {
    background: var(--color-primary-bg);
    opacity: 0.9;
  }
}
</style>
