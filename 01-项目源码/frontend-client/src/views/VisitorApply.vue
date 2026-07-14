<template>
  <div>
    <h2 class="page-title">{{ t('visitorApply.title') }}</h2>
    <div v-if="list.length" class="list">
      <div v-for="item in list" :key="item.id" class="card item-card">
        <h3>{{ item.visitorName }}</h3>
        <p v-if="item.reason" class="body">{{ t('visitorApply.reason') }}：{{ item.reason }}</p>
        <p class="meta">
          <span v-if="item.visitDate">{{ t('visitorApply.visitDate') }}：{{ item.visitDate }}</span>
          <span v-if="item.visitorPhone">{{ t('visitorApply.visitorPhone') }}：{{ item.visitorPhone }}</span>
          <span v-if="item.status">{{ t('common.status') }}：{{ t(`status.${item.status}`) }}</span>
        </p>
      </div>
    </div>
    <p v-else class="empty card">{{ t('common.empty') }}</p>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'

const { t } = useI18n()
const list = ref([])

onMounted(async () => { list.value = await request.get('/visitors') })
</script>

<style scoped>
.item-card { margin-bottom: 12px; }
.item-card h3 { font-size: 15px; margin-bottom: 8px; }
.body { font-size: 14px; color: var(--color-text); line-height: 1.6; margin-bottom: 8px; }
.meta { font-size: 13px; color: var(--color-text-secondary); line-height: 1.5; display: flex; flex-wrap: wrap; gap: 4px 12px; }
</style>
