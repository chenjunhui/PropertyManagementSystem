<template>
  <div>
    <h2 class="page-title">{{ t('nav.logs') }}</h2>
    <div class="card">
      <div class="toolbar">
        <div class="toolbar-left">
          <select v-model="filterModule" class="select" @change="loadLogs">
            <option value="">{{ t('common.all') }}</option>
            <option value="认证">认证</option>
            <option value="业主">业主</option>
            <option value="报修">报修</option>
          </select>
        </div>
      </div>
      <table class="table">
        <thead>
          <tr>
            <th>{{ t('log.time') }}</th>
            <th>{{ t('log.module') }}</th>
            <th>{{ t('log.operation') }}</th>
            <th>{{ t('log.operator') }}</th>
            <th>{{ t('log.detail') }}</th>
            <th>{{ t('common.status') }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in logs" :key="item.id">
            <td>{{ formatTime(item.createdAt) }}</td>
            <td>{{ item.module }}</td>
            <td>{{ item.operation }}</td>
            <td>{{ item.operator || '-' }}</td>
            <td>{{ item.detail || '-' }}</td>
            <td>
              <span :class="['tag', item.status === 1 ? 'success' : 'error']">
                {{ item.status === 1 ? t('log.success') : t('log.fail') }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="!logs.length" class="empty">{{ t('common.empty') }}</div>
      <div class="pagination" v-if="total > pageSize">
        <button class="btn btn-ghost" :disabled="page === 0" @click="page--; loadLogs()">{{ t('common.prev') }}</button>
        <span>{{ page + 1 }} / {{ Math.ceil(total / pageSize) }}</span>
        <button class="btn btn-ghost" :disabled="(page + 1) * pageSize >= total" @click="page++; loadLogs()">{{ t('common.next') }}</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'

const { t } = useI18n()
const logs = ref([])
const total = ref(0)
const page = ref(0)
const pageSize = 20
const filterModule = ref('')

async function loadLogs() {
  const params = { page: page.value, size: pageSize }
  if (filterModule.value) params.module = filterModule.value
  const res = await request.get('/logs', { params })
  logs.value = res.content || []
  total.value = res.totalElements || 0
}

function formatTime(t) {
  if (!t) return '-'
  return t.replace('T', ' ').substring(0, 19)
}

onMounted(loadLogs)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.toolbar-left { display: flex; gap: 12px; }
.toolbar-left .select { width: auto; min-width: 120px; }
.tag { font-size: 12px; padding: 2px 8px; border-radius: 10px; }
.tag.success { background: var(--color-primary-bg); color: var(--color-primary); }
.tag.error { background: rgba(239,68,68,0.1); color: #DC2626; }
.pagination { display: flex; align-items: center; justify-content: center; gap: 16px; margin-top: 16px; }
.pagination span { font-size: 13px; color: var(--color-text-secondary); }
</style>
