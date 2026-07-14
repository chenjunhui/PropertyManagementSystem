<template>
  <div class="layout">
    <div class="top-controls">
      <LangSwitcher />
      <ThemeSwitcher />
    </div>
    <header class="app-header">
      <div class="header-inner">
        <div class="brand">
          <UserAvatar :name="userStore.user?.name" :avatar="userStore.user?.avatar" size="md" variant="light" />
          <div class="brand-icon" v-html="brandIconSvg"></div>
          <div>
            <div class="brand-title">{{ t('auth.propertySystem') }}</div>
            <div class="brand-sub">{{ t('auth.clientPortal') }} · {{ userStore.user?.name || t('role.OWNER') }}</div>
          </div>
        </div>
      </div>
      <nav class="pill-nav">
        <router-link v-for="tab in tabs" :key="tab.path" :to="tab.path" class="pill-item">
          <span class="pill-icon" v-html="tab.icon"></span>
          {{ tab.label }}
        </router-link>
      </nav>
    </header>
    <main class="main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../stores/user'
import { brandIconSvg } from '../utils/brandIcon'
import UserAvatar from '../components/UserAvatar.vue'
import LangSwitcher from '../components/LangSwitcher.vue'
import ThemeSwitcher from '../components/ThemeSwitcher.vue'

const { t } = useI18n()
const userStore = useUserStore()

const tabs = computed(() => [
  { path: '/home', label: t('nav.home'), icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>' },
  { path: '/my-property', label: t('nav.myProperty'), icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>' },
  { path: '/announcements', label: t('nav.announcements'), icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>' },
  { path: '/visitor-apply', label: t('nav.visitorApply'), icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="8.5" cy="7" r="4"/><line x1="20" y1="8" x2="20" y2="14"/><line x1="23" y1="11" x2="17" y2="11"/></svg>' },
  { path: '/repair', label: t('nav.repair'), icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>' },
  { path: '/profile', label: t('nav.profile'), icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>' }
])
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.top-controls {
  position: fixed;
  top: 16px;
  right: 16px;
  z-index: 100;
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.18);
  backdrop-filter: blur(12px);
  padding: 8px 12px;
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.25);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.app-header {
  background: linear-gradient(135deg, var(--color-primary-dark) 0%, var(--color-primary) 50%, var(--color-primary-light) 100%);
  color: #fff;
  position: sticky;
  top: 0;
  z-index: 50;
  box-shadow: 0 4px 20px var(--shadow-color);
}

.header-inner {
  max-width: 720px;
  margin: 0 auto;
  padding: 20px 20px 12px;
  width: 100%;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand-icon {
  width: 44px;
  height: 44px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.brand-title { font-size: 18px; font-weight: 600; }
.brand-sub { font-size: 12px; opacity: 0.85; margin-top: 2px; }

.pill-nav {
  max-width: 720px;
  margin: 0 auto;
  padding: 0 16px 14px;
  display: flex;
  gap: 8px;
  overflow-x: auto;
  scrollbar-width: none;
  width: 100%;
}

.pill-nav::-webkit-scrollbar { display: none; }

.pill-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.75);
  text-decoration: none;
  white-space: nowrap;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  transition: all var(--transition);
}

.pill-item:focus-visible {
  outline: none;
  background: rgba(255, 255, 255, 0.25);
  color: #fff;
  border-color: rgba(255, 255, 255, 0.4);
}

.pill-item:focus-visible.router-link-active {
  background: #fff;
  color: var(--color-primary-dark);
}

.pill-item:hover {
  background: rgba(255, 255, 255, 0.18);
  color: #fff;
}

.pill-item.router-link-active {
  background: #fff;
  color: var(--color-primary-dark);
  border-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
}

.pill-icon { display: flex; }

.main {
  flex: 1;
  max-width: 720px;
  width: 100%;
  margin: 0 auto;
  padding: 20px 16px 32px;
}
</style>
