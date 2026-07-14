<template>
  <select class="lang-switcher" :value="locale" @change="switchLang">
    <option value="zh-CN">中文</option>
    <option value="en">English</option>
    <option value="ja">日本語</option>
  </select>
</template>

<script setup>
import { useI18n } from 'vue-i18n'

const { locale } = useI18n()

function switchLang(e) {
  locale.value = e.target.value
  localStorage.setItem('lang', e.target.value)
}
</script>

<style scoped>
.lang-switcher {
  padding: 4px 8px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  font-size: 12px;
  cursor: pointer;
  outline: none;
}
.lang-switcher:focus {
  border-color: #fff;
  box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.2);
}
.lang-switcher option {
  color: #333;
  background: #fff;
}
</style>
