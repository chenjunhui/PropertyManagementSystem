<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('repair.title') }}</h2>
      <select v-model="statusFilter" class="select" @change="load">
        <option value="">{{ t('common.status') }}</option>
        <option value="PENDING">{{ t('status.PENDING') }}</option>
        <option value="PROCESSING">{{ t('status.PROCESSING') }}</option>
        <option value="DONE">{{ t('status.DONE') }}</option>
      </select>
    </div>
    <div class="card">
      <table class="table">
        <thead>
          <tr><th>{{ t('repair.title2') }}</th><th>{{ t('repair.owner') }}</th><th>{{ t('repair.room') }}</th><th>{{ t('repair.submitDate') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.title }}</td>
            <td>{{ ownerMap[item.ownerId] || item.ownerId }}</td>
            <td>{{ unitMap[item.roomId] || item.roomId }}</td>
            <td>{{ item.submitDate || '—' }}</td>
            <td><span :class="['tag', statusClass(item.status)]">{{ t(`status.${item.status}`) }}</span></td>
            <td>
              <button v-if="item.status === 'PENDING'" class="btn btn-ghost" @click="setStatus(item, 'PROCESSING')">{{ t('status.PROCESSING') }}</button>
              <button v-if="item.status === 'PROCESSING'" class="btn btn-primary" @click="setStatus(item, 'DONE')">{{ t('status.DONE') }}</button>
              <button class="btn btn-danger" @click="remove(item)">{{ t('common.delete') }}</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="6" class="empty">{{ t('common.empty') }}</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'
import { showConfirm } from '../composables/useConfirm'

const { t } = useI18n()
const list = ref([])
const owners = ref([])
const units = ref([])
const buildings = ref([])
const statusFilter = ref('')

const ownerMap = computed(() => Object.fromEntries(owners.value.map(r => [r.id, `${r.name}（${r.ownerNo}）`])))
const unitMap = computed(() => {
  const bm = Object.fromEntries(buildings.value.map(b => [b.id, b.buildingName]))
  return Object.fromEntries(units.value.map(u => [u.id, `${bm[u.buildingId] || ''} ${u.unitNo}`.trim()]))
})

function statusLabel(s) { return t(`status.${s}`) }
function statusClass(s) { return { PENDING: 'pending', PROCESSING: 'processing', DONE: 'done' }[s] || '' }

async function loadMeta() {
  [owners.value, units.value, buildings.value] = await Promise.all([
    request.get('/owners'), request.get('/units'), request.get('/buildings')
  ])
}

async function load() {
  list.value = await request.get('/repairs', { params: { status: statusFilter.value || undefined } })
}

async function setStatus(item, status) {
  await request.put(`/repairs/${item.id}/status`, { status })
  load()
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteRepair'))
  if (!ok) return
  await request.delete(`/repairs/${item.id}`)
  load()
}

onMounted(async () => { await loadMeta(); load() })
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.table .btn { margin-right: 8px; padding: 4px 10px; font-size: 13px; }
.empty { text-align: center; color: var(--color-text-secondary); padding: 24px !important; }
.tag.pending { background: var(--color-primary-bg); color: var(--color-primary); }
.tag.processing { background: rgba(217,119,6,0.12); color: #D97706; }
.tag.done { background: var(--color-primary-bg); color: var(--color-primary); }
</style>
