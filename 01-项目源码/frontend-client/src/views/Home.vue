<template>
  <div>
    <div class="hero">
      <p class="hero-greet">{{ t('home.welcome') }}，{{ userStore.user?.name || t('role.OWNER') }}</p>
      <h2 class="hero-title">{{ t('auth.systemDesc') }}</h2>
      <p class="hero-desc">{{ t('auth.feature1') }}</p>
    </div>
    <div class="quick-grid">
      <router-link to="/my-property" class="quick-card">
        <div class="quick-icon dorm">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
        </div>
        <div class="quick-body">
          <div class="quick-title">{{ t('home.myUnit') }}</div>
          <div class="quick-desc">{{ t('home.unitInfo') }}</div>
        </div>
        <span class="quick-arrow">→</span>
      </router-link>
      <router-link to="/repair" class="quick-card accent">
        <div class="quick-icon repair">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>
        </div>
        <div class="quick-body">
          <div class="quick-title">{{ t('home.repairApply') }}</div>
          <div class="quick-desc">{{ t('auth.feature2') }}</div>
        </div>
        <span class="quick-arrow">→</span>
      </router-link>
      <router-link to="/profile" class="quick-card">
        <div class="quick-icon profile">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        </div>
        <div class="quick-body">
          <div class="quick-title">{{ t('profile.title') }}</div>
          <div class="quick-desc">{{ t('auth.feature3') }}</div>
        </div>
        <span class="quick-arrow">→</span>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../stores/user'
const { t } = useI18n()
const userStore = useUserStore()
</script>

<style scoped>
.hero {
  background: linear-gradient(135deg, var(--color-primary-bg) 0%, var(--color-primary-bg-deep) 100%);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 24px;
  margin-bottom: 20px;
}

.hero-greet {
  font-size: 13px;
  color: var(--color-primary);
  font-weight: 500;
  margin-bottom: 6px;
}

.hero-title {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 8px;
  color: var(--color-text);
}

.hero-desc {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.5;
}

.quick-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.quick-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  text-decoration: none;
  color: inherit;
  box-shadow: var(--shadow);
  transition: all var(--transition);
}

.quick-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
  border-color: var(--color-primary-border);
}

.quick-card.accent {
  background: linear-gradient(135deg, var(--color-primary-bg), var(--color-primary-bg-deep));
}

.quick-icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: var(--color-primary);
}

.quick-icon.dorm { background: var(--color-primary-bg); }
.quick-icon.repair { background: rgba(217, 119, 6, 0.15); }
.quick-icon.profile { background: rgba(59, 130, 246, 0.15); }

.quick-body { flex: 1; }

.quick-title {
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 4px;
}

.quick-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.quick-arrow {
  color: var(--color-primary);
  font-size: 18px;
  opacity: 0.6;
}
</style>
