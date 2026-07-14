import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

const THEMES = {
  emerald: {
    name: '翡翠绿',
    primary: '#059669',
    primaryLight: '#10B981',
    primaryDark: '#047857',
    primaryBright: '#34D399',
    primaryBg: '#ECFDF5',
    primaryBgDeep: '#D1FAE5',
    primaryBorder: '#A7F3D0',
    bg: '#F0FDF4',
    surface: '#FFFFFF',
    text: '#064E3B',
    textSecondary: '#64748B',
    shadow: 'rgba(5, 150, 105, 0.15)'
  },
  blue: {
    name: '天空蓝',
    primary: '#2563EB',
    primaryLight: '#3B82F6',
    primaryDark: '#1D4ED8',
    primaryBright: '#60A5FA',
    primaryBg: '#EFF6FF',
    primaryBgDeep: '#DBEAFE',
    primaryBorder: '#93C5FD',
    bg: '#EFF6FF',
    surface: '#FFFFFF',
    text: '#1E3A5F',
    textSecondary: '#64748B',
    shadow: 'rgba(37, 99, 235, 0.15)'
  },
  purple: {
    name: '优雅紫',
    primary: '#7C3AED',
    primaryLight: '#8B5CF6',
    primaryDark: '#6D28D9',
    primaryBright: '#A78BFA',
    primaryBg: '#F5F3FF',
    primaryBgDeep: '#EDE9FE',
    primaryBorder: '#C4B5FD',
    bg: '#F5F3FF',
    surface: '#FFFFFF',
    text: '#3B0764',
    textSecondary: '#64748B',
    shadow: 'rgba(124, 58, 237, 0.15)'
  },
  amber: {
    name: '琥珀橙',
    primary: '#D97706',
    primaryLight: '#F59E0B',
    primaryDark: '#B45309',
    primaryBright: '#FBBF24',
    primaryBg: '#FFFBEB',
    primaryBgDeep: '#FEF3C7',
    primaryBorder: '#FDE68A',
    bg: '#FFFBEB',
    surface: '#FFFFFF',
    text: '#78350F',
    textSecondary: '#64748B',
    shadow: 'rgba(217, 119, 6, 0.15)'
  },
  black: {
    name: '石墨灰',
    primary: '#475569',
    primaryLight: '#64748B',
    primaryDark: '#334155',
    primaryBright: '#94A3B8',
    primaryBg: '#F1F5F9',
    primaryBgDeep: '#E2E8F0',
    primaryBorder: '#CBD5E1',
    bg: '#F8FAFC',
    surface: '#FFFFFF',
    text: '#1E293B',
    textSecondary: '#64748B',
    shadow: 'rgba(71, 85, 105, 0.12)'
  }
}

export const useThemeStore = defineStore('theme', () => {
  const current = ref(localStorage.getItem('theme') || 'emerald')
  const themes = THEMES

  function applyTheme(key) {
    const t = THEMES[key]
    if (!t) return
    const root = document.documentElement
    root.style.setProperty('--color-primary', t.primary)
    root.style.setProperty('--color-primary-light', t.primaryLight)
    root.style.setProperty('--color-primary-dark', t.primaryDark)
    root.style.setProperty('--color-primary-bright', t.primaryBright)
    root.style.setProperty('--color-primary-bg', t.primaryBg)
    root.style.setProperty('--color-primary-bg-deep', t.primaryBgDeep)
    root.style.setProperty('--color-primary-border', t.primaryBorder)
    root.style.setProperty('--color-bg', t.bg)
    root.style.setProperty('--color-surface', t.surface)
    root.style.setProperty('--color-text', t.text)
    root.style.setProperty('--color-text-secondary', t.textSecondary)
    root.style.setProperty('--shadow-color', t.shadow)
  }

  function setTheme(key) {
    current.value = key
    localStorage.setItem('theme', key)
    applyTheme(key)
  }

  // Apply on load
  applyTheme(current.value)

  return { current, themes, setTheme }
})
