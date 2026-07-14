<template>
  <div>
    <h2 class="page-title">{{ t('announcements.title') }}</h2>
    <div v-if="list.length" class="list">
      <div v-for="item in list" :key="item.id" class="card item-card">
        <h3>{{ item.title }}</h3>
        <p v-if="item.content" class="body">{{ item.content }}</p>
        <p class="meta">
          <span v-if="item.publishDate">{{ t('announcements.publishDate') }}：{{ item.publishDate }}</span>
          <span v-if="item.publisher">{{ t('announcements.publisher') }}：{{ item.publisher }}</span>
        </p>
      </div>
    </div>
    <p v-else class="empty card">{{ t('announcements.noAnnouncements') }}</p>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'

const { t } = useI18n()
const list = ref([])

onMounted(async () => { list.value = await request.get('/announcements') })
</script>

<style scoped>
.item-card { margin-bottom: 12px; }
.item-card h3 { font-size: 15px; margin-bottom: 8px; }
.body { font-size: 14px; color: var(--color-text); line-height: 1.6; margin-bottom: 8px; }
.meta { font-size: 13px; color: var(--color-text-secondary); line-height: 1.5; display: flex; flex-wrap: wrap; gap: 4px 12px; }
</style>
