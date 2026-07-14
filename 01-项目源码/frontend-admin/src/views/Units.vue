<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('unit.title') }}</h2>
      <div class="actions">
        <select v-model="buildingFilter" class="select" @change="load">
          <option :value="null">{{ t('unit.building') }}</option>
          <option v-for="b in buildings" :key="b.id" :value="b.id">{{ b.buildingName }}</option>
        </select>
        <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}{{ t('unit.title') }}</button>
      </div>
    </div>
    <div class="card">
      <table class="table">
        <thead>
          <tr><th>{{ t('unit.unitNo') }}</th><th>{{ t('unit.building') }}</th><th>{{ t('unit.floor') }}</th><th>{{ t('unit.unitType') }}</th><th>{{ t('unit.area') }}</th><th>{{ t('unit.ownerCount') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.unitNo }}</td>
            <td>{{ buildingMap[item.buildingId] || '—' }}</td>
            <td>{{ item.floor ?? '—' }}</td>
            <td>{{ item.unitType || '—' }}</td>
            <td>{{ item.areaSqm ?? '—' }}</td>
            <td>{{ item.ownerCount ?? 0 }}</td>
            <td><span :class="['tag', statusClass(item.status)]">{{ t(`unitStatus.${item.status}`) }}</span></td>
            <td>
              <button class="btn btn-ghost" @click="openForm(item)">{{ t('common.edit') }}</button>
              <button class="btn btn-danger" @click="remove(item)">{{ t('common.delete') }}</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="8" class="empty">{{ t('common.empty') }}</td></tr>
        </tbody>
      </table>
    </div>
    <div v-if="showModal" class="modal-mask" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ form.id ? t('common.edit') : t('common.add') }}{{ t('unit.title') }}</h3>
        <div class="form-group">
          <label>{{ t('unit.building') }} *</label>
          <select v-model="form.buildingId" class="select">
            <option v-for="b in buildings" :key="b.id" :value="b.id">{{ b.buildingName }}</option>
          </select>
        </div>
        <div class="form-group"><label>{{ t('unit.unitNo') }} *</label><input v-model="form.unitNo" class="input" maxlength="20" placeholder="1-101" /></div>
        <div class="form-group"><label>{{ t('unit.floor') }}</label><input v-model.number="form.floor" type="number" min="1" class="input" /></div>
        <div class="form-group">
          <label>{{ t('unit.unitType') }}</label>
          <select v-model="form.unitType" class="select">
            <option value="">-</option>
            <option>{{ t('unit.unitTypes.一室一厅') }}</option><option>{{ t('unit.unitTypes.两室一厅') }}</option><option>{{ t('unit.unitTypes.三室两厅') }}</option><option>{{ t('unit.unitTypes.复式') }}</option><option>{{ t('unit.unitTypes.商铺') }}</option>
          </select>
        </div>
        <div class="form-group"><label>{{ t('unit.area') }} *</label><input v-model.number="form.areaSqm" type="number" min="1" step="0.01" class="input" /></div>
        <div class="form-group"><label>{{ t('unit.ownerCount') }}</label><input v-model.number="form.ownerCount" type="number" min="0" class="input" /></div>
        <div class="form-group">
          <label>{{ t('common.status') }}</label>
          <select v-model.number="form.status" class="select">
            <option :value="1">{{ t('unitStatus.1') }}</option><option :value="2">{{ t('unitStatus.2') }}</option><option :value="0">{{ t('unitStatus.0') }}</option>
          </select>
        </div>
        <p v-if="formError" class="err">{{ formError }}</p>
        <div class="modal-actions">
          <button class="btn btn-ghost" @click="showModal = false">{{ t('common.cancel') }}</button>
          <button class="btn btn-primary" @click="save">{{ t('common.save') }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'
import { showConfirm } from '../composables/useConfirm'

const { t } = useI18n()
const list = ref([])
const buildings = ref([])
const buildingFilter = ref(null)
const showModal = ref(false)
const formError = ref('')
const form = reactive({ id: null, buildingId: null, unitNo: '', floor: 1, unitType: '', areaSqm: 80, ownerCount: 0, status: 1, description: '' })
const buildingMap = computed(() => Object.fromEntries(buildings.value.map(b => [b.id, b.buildingName])))

function statusLabel(s) { return t(`unitStatus.${s}`) }
function statusClass(s) { return s === 1 ? 'on' : s === 2 ? 'warn' : 'off' }

async function loadBuildings() { buildings.value = await request.get('/buildings') }

async function load() {
  const params = buildingFilter.value ? { buildingId: buildingFilter.value } : {}
  list.value = await request.get('/units', { params })
}

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, buildingId: buildings.value[0]?.id ?? null, unitNo: '', floor: 1, unitType: '', areaSqm: 80, ownerCount: 0, status: 1, description: '' })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  if (!form.buildingId || !form.unitNo?.trim()) { formError.value = t('validation.buildingRequired'); return }
  try {
    if (form.id) await request.put(`/units/${form.id}`, form)
    else await request.post('/units', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteUnit'))
  if (!ok) return
  try { await request.delete(`/units/${item.id}`); load() } catch (e) { formError.value = e.message }
}

onMounted(async () => { await loadBuildings(); load() })
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.actions { display: flex; gap: 8px; align-items: center; }
.table .btn { margin-right: 8px; padding: 4px 10px; font-size: 13px; }
.tag.warn { background: #FEF3C7; color: #B45309; }
.empty { text-align: center; color: var(--color-text-secondary); padding: 24px !important; }
.err { color: var(--color-danger); font-size: 13px; }
</style>
