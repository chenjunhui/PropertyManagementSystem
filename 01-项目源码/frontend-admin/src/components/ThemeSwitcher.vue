<template>
  <div class="theme-switcher">
    <span class="theme-label">{{ t('common.theme') }}</span>
    <div class="theme-options">
      <button
        v-for="(theme, key) in themes"
        :key="key"
        class="theme-dot"
        :class="{ active: current === key }"
        :style="{
          background: theme.primary,
          borderColor: current === key ? theme.primaryBorder : 'transparent',
          boxShadow: current === key ? `0 0 0 2px ${theme.primaryBg}` : 'none'
        }"
        :title="theme.name"
        @click="setTheme(key)"
      />
    </div>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { storeToRefs } from 'pinia'
import { useThemeStore } from '../stores/theme'

const { t } = useI18n()
const store = useThemeStore()
const { current } = storeToRefs(store)
const { themes, setTheme } = store
</script>

<style scoped>
.theme-switcher {
  display: flex;
  align-items: center;
  gap: 8px;
}
.theme-label {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.9);
}
.theme-options {
  display: flex;
  gap: 6px;
}
.theme-dot {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s;
  padding: 0;
}
.theme-dot:hover {
  transform: scale(1.15);
}
</style>
