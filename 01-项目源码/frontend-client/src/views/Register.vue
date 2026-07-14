<template>
  <div class="register-page">
    <div class="register-overlay"></div>
    <div class="top-controls">
      <LangSwitcher />
      <ThemeSwitcher />
    </div>
    <div class="register-shell">
      <aside class="register-hero">
        <p class="hero-kicker">Join Community</p>
        <h2>{{ t('register.title') }}</h2>
        <p class="hero-desc">{{ t('auth.systemDesc') }}</p>
        <ul class="step-list">
          <li><span class="step-num">1</span>{{ t('register.username') }}</li>
          <li><span class="step-num">2</span>{{ t('register.submit') }}</li>
          <li><span class="step-num">3</span>{{ t('auth.login') }}</li>
        </ul>
      </aside>
      <div class="register-card">
        <div class="register-header">
          <div class="brand-mark" v-html="brandIconSvg"></div>
          <span class="portal-badge">{{ t('register.title') }}</span>
          <h1>{{ t('auth.propertySystem') }}</h1>
          <p>{{ t('auth.systemDesc') }}</p>
        </div>
        <form @submit.prevent="handleRegister">
          <div class="form-row">
            <div class="form-group">
              <label>{{ t('register.username') }} *</label>
              <input v-model="form.username" class="input" maxlength="50" :placeholder="t('register.usernamePlaceholder')" required />
            </div>
            <div class="form-group">
              <label>{{ t('register.password') }} *</label>
              <input v-model="form.password" type="password" class="input" maxlength="50" :placeholder="t('register.passwordPlaceholder')" required />
            </div>
            <div class="form-group">
              <label>{{ t('register.ownerNo') }} *</label>
              <input v-model="form.ownerNo" class="input" maxlength="20" :placeholder="t('register.ownerNoPlaceholder')" required />
              <span v-if="errors.ownerNo" class="err">{{ errors.ownerNo }}</span>
            </div>
            <div class="form-group">
              <label>{{ t('register.name') }} *</label>
              <input v-model="form.name" class="input" maxlength="50" :placeholder="t('register.namePlaceholder')" required />
            </div>
            <div class="form-group">
              <label>{{ t('register.gender') }} *</label>
              <select v-model="form.gender" class="select">
                <option value="男">{{ t('register.male') }}</option>
                <option value="女">{{ t('register.female') }}</option>
              </select>
            </div>
            <div class="form-group">
              <label>{{ t('register.phone') }} *</label>
              <input v-model="form.phone" class="input" maxlength="11" :placeholder="t('register.phonePlaceholder')" />
              <span v-if="errors.phone" class="err">{{ errors.phone }}</span>
            </div>
            <div class="form-group full-width">
              <label>{{ t('register.email') }}</label>
              <input v-model="form.email" class="input" maxlength="100" :placeholder="t('register.emailPlaceholder')" />
              <span v-if="errors.email" class="err">{{ errors.email }}</span>
            </div>
          </div>
          <p v-if="error" class="error">{{ error }}</p>
          <button class="btn btn-primary" type="submit" :disabled="loading">{{ loading ? t('register.submitting') : t('register.submit') }}</button>
        </form>
        <p class="login-link">已有账号？<router-link to="/login">返回登录</router-link></p>
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
import { validatePhone, validateEmail, validateOwnerNo } from '../utils/validators'
import LangSwitcher from '../components/LangSwitcher.vue'
import ThemeSwitcher from '../components/ThemeSwitcher.vue'

const { t } = useI18n()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const error = ref('')
const errors = reactive({ ownerNo: '', phone: '', email: '' })
const form = reactive({ username: '', password: '', ownerNo: '', name: '', gender: '男', phone: '', email: '' })

function validate() {
  errors.ownerNo = validateOwnerNo(form.ownerNo)
  errors.phone = validatePhone(form.phone, true)
  errors.email = validateEmail(form.email)
  if (!form.password || form.password.length < 6) { error.value = t('validation.passwordMinLength'); return false }
  error.value = ''
  return !errors.ownerNo && !errors.phone && !errors.email
}

async function handleRegister() {
  if (!validate()) return
  loading.value = true
  try {
    await userStore.register({ ...form })
    router.push('/login')
  } catch (e) { error.value = e.message || t('auth.loginFailed') }
  finally { loading.value = false }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  position: relative;
  background: url('/resources/images/login/register-bg.svg') center/cover no-repeat fixed;
  background-color: var(--color-primary-bg);
}

.register-overlay {
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

.register-shell {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  display: grid;
  grid-template-columns: minmax(260px, 1fr) min(560px, 100%);
  gap: clamp(24px, 4vw, 56px);
  align-items: center;
  max-width: 1180px;
  margin: 0 auto;
  padding: clamp(24px, 5vw, 48px) clamp(24px, 5vw, 64px);
}

.register-hero {
  color: #064e3b;
  animation: fadeUp 0.5s ease-out;
}

.hero-kicker {
  font-size: 13px;
  letter-spacing: 0.14em;
  text-transform: uppercase;
  color: var(--color-primary);
  margin-bottom: 14px;
  font-weight: 600;
}

.register-hero h2 {
  font-size: clamp(28px, 4vw, 38px);
  font-weight: 800;
  line-height: 1.25;
  margin-bottom: 16px;
  letter-spacing: -0.02em;
}

.hero-desc {
  font-size: 15px;
  line-height: 1.7;
  color: #475569;
  margin-bottom: 28px;
  max-width: 360px;
}

.step-list {
  list-style: none;
  display: grid;
  gap: 14px;
}

.step-list li {
  display: flex;
  align-items: center;
  gap: 14px;
  font-size: 14px;
  color: var(--color-primary-dark);
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.7);
  border: 1px solid var(--color-primary-border);
  border-radius: 12px;
}

.step-num {
  width: 28px;
  height: 28px;
  flex-shrink: 0;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
  color: #fff;
  font-size: 13px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}

.register-card {
  width: 100%;
  padding: 36px 32px;
  background: rgba(255, 255, 255, 0.94);
  backdrop-filter: blur(18px);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 22px;
  box-shadow: 0 20px 56px var(--shadow-color);
  animation: slideIn 0.45s ease-out;
}

@keyframes slideIn {
  from { opacity: 0; transform: translateY(16px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(14px); }
  to { opacity: 1; transform: translateY(0); }
}

.brand-mark {
  width: 48px;
  height: 48px;
  margin-bottom: 14px;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
  color: #fff;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px var(--shadow-color);
}

.register-header { margin-bottom: 24px; }

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

.register-header h1 { font-size: 24px; font-weight: 700; color: #064e3b; margin-bottom: 6px; }
.register-header p { font-size: 14px; color: #64748b; }

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 6px;
}

.input, .select {
  width: 100%;
  padding: 11px 14px;
  border: 1px solid var(--color-primary-border);
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
  background: #fff;
}

.input:focus, .select:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--shadow-color);
  outline: none;
}

.full-width { grid-column: 1 / -1; }

.err { color: #dc2626; font-size: 12px; margin-top: 4px; display: block; }

.error {
  color: #dc2626;
  font-size: 13px;
  margin: 12px 0;
  padding: 8px 12px;
  background: rgba(220, 38, 38, 0.06);
  border-radius: 8px;
}

.btn-primary {
  width: 100%;
  padding: 13px;
  margin-top: 8px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
  border: none;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #64748b;
}

.login-link a { color: var(--color-primary); font-weight: 600; text-decoration: none; }

@media (max-width: 900px) {
  .register-shell { grid-template-columns: 1fr; max-width: 560px; }
  .register-hero { display: none; }
}

@media (max-width: 520px) {
  .form-row { grid-template-columns: 1fr; }
  .full-width { grid-column: auto; }
}
</style>
