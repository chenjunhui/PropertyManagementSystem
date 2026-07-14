<template>
  <div class="layout">
    <div class="top-controls">
      <LangSwitcher />
      <ThemeSwitcher />
    </div>
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-icon" v-html="brandIconSvg"></div>
        <div>
          <div class="brand-title">{{ t('auth.propertySystem') }}</div>
          <div class="brand-sub">{{ t('auth.adminPortal') }} · {{ roleLabel }}</div>
        </div>
      </div>
      <nav class="nav">
        <router-link v-for="item in navMenus" :key="item.path" :to="item.path" class="nav-item">
          <span class="nav-icon" v-html="item.icon"></span>
          <span>{{ item.label }}</span>
        </router-link>
      </nav>
      <div class="sidebar-footer">
        <router-link to="/profile" class="user-chip">
          <UserAvatar :name="userStore.user?.name" :avatar="userStore.user?.avatar" size="sm" variant="light" />
          <span class="user-name">{{ userStore.user?.name }}</span>
        </router-link>
        <button class="btn-logout" @click="logout">{{ t('auth.logout') }}</button>
      </div>
    </aside>
    <div class="main">
      <header class="top-bar">
        <h1 class="page-heading">{{ currentTitle }}</h1>
        <div class="top-bar-meta">{{ t('auth.propertySystem') }}</div>
      </header>
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../stores/user'
import { menuIcon } from '../utils/menuIcons'
import { brandIconSvg } from '../utils/brandIcon'
import UserAvatar from '../components/UserAvatar.vue'
import LangSwitcher from '../components/LangSwitcher.vue'
import ThemeSwitcher from '../components/ThemeSwitcher.vue'

const { t } = useI18n()
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const roleLabel = computed(() => t(`role.${userStore.user?.role}`))

const menuTitleMap = {
  '/dashboard': 'nav.dashboard',
  '/buildings': 'nav.buildings',
  '/units': 'nav.units',
  '/owners': 'nav.owners',
  '/repairs': 'nav.repairs',
  '/inspection-records': 'nav.inspection',
  '/visitors': 'nav.visitors',
  '/announcements': 'nav.announcements',
  '/property-fees': 'nav.propertyFees',
  '/profile': 'nav.profile'
}

const navMenus = computed(() =>
  userStore.menus.map(m => ({
    path: m.path,
    label: menuTitleMap[m.path] ? t(menuTitleMap[m.path]) : m.title,
    icon: menuIcon(m.icon)
  }))
)

const currentTitle = computed(() =>
  navMenus.value.find(m => m.path === route.path)?.label || t('nav.workbench')
)

onMounted(async () => {
  if (userStore.user && !userStore.menus.length) {
    await userStore.fetchMenus(userStore.user.role)
  }
})

function logout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
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

.sidebar {
  width: var(--sidebar-width);
  background: linear-gradient(180deg, var(--color-primary-dark) 0%, var(--color-primary) 55%, var(--color-primary-dark) 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 40;
  box-shadow: 4px 0 24px var(--shadow-color);
}

.brand {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 24px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
}

.brand-icon {
  width: 42px;
  height: 42px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.brand-title { font-weight: 600; font-size: 15px; line-height: 1.3; }
.brand-sub { font-size: 11px; opacity: 0.75; margin-top: 2px; }

.nav {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 16px 12px;
  overflow-y: auto;
}

.nav::-webkit-scrollbar {
  width: 4px;
}

.nav::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
}

.nav::-webkit-scrollbar-track {
  background: transparent;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 11px 14px;
  border-radius: 10px;
  color: rgba(255, 255, 255, 0.75);
  font-size: 14px;
  transition: all var(--transition);
  border-left: 3px solid transparent;
}

.nav-item:focus-visible {
  outline: none;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.nav-item:focus-visible.router-link-active {
  background: rgba(255, 255, 255, 0.25);
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.nav-item.router-link-active {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  border-left-color: var(--color-primary-bright);
  font-weight: 500;
}

.nav-icon { display: flex; opacity: 0.9; }

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.12);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.user-chip {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 10px;
  color: #fff;
  text-decoration: none;
  background: rgba(255, 255, 255, 0.08);
}

.user-chip:hover { background: rgba(255, 255, 255, 0.14); }

.user-name { font-size: 13px; }

.btn-logout {
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.25);
  color: rgba(255, 255, 255, 0.85);
  padding: 8px;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  font-family: inherit;
  transition: all var(--transition);
}

.btn-logout:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.main {
  flex: 1;
  margin-left: var(--sidebar-width);
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.top-bar {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  padding: 20px 28px;
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 16px;
}

.page-heading {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text);
}

.top-bar-meta {
  font-size: 13px;
  color: var(--color-text-secondary);
  padding: 4px 12px;
  background: var(--color-primary-bg, #ECFDF5);
  border-radius: 20px;
}

.content {
  flex: 1;
  padding: 24px 28px;
}
</style>
