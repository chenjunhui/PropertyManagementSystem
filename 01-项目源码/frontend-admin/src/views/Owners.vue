<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('owner.title') }}</h2>
      <div class="actions">
        <input v-model="keyword" class="input search" :placeholder="t('common.search') + '...'" maxlength="50" @keyup.enter="load" />
        <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}{{ t('owner.title') }}</button>
      </div>
    </div>
    <div class="card">
      <table class="table">
        <thead>
          <tr><th>{{ t('owner.ownerNo') }}</th><th>{{ t('owner.name') }}</th><th>{{ t('owner.gender') }}</th><th>{{ t('owner.phone') }}</th><th>{{ t('owner.room') }}</th><th>{{ t('owner.propertyCert') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.ownerNo }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.gender || '—' }}</td>
            <td>{{ item.phone || '—' }}</td>
            <td>{{ unitLabel(item.roomId) }}</td>
            <td>{{ item.propertyCert || '—' }}</td>
            <td><span :class="['tag', item.status === 1 ? 'on' : 'off']">{{ item.status === 1 ? t('common.enable') : t('common.disable') }}</span></td>
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
      <div class="modal modal-lg">
        <h3>{{ form.id ? t('common.edit') : t('common.add') }}{{ t('owner.title') }}</h3>
        <div class="form-row">
          <div class="form-group"><label>{{ t('owner.ownerNo') }} *</label><input v-model="form.ownerNo" class="input" maxlength="20" :readonly="!!form.id" /></div>
          <div class="form-group"><label>{{ t('owner.name') }} *</label><input v-model="form.name" class="input" maxlength="50" /></div>
          <div class="form-group">
            <label>{{ t('owner.gender') }}</label>
            <select v-model="form.gender" class="select"><option>{{ t('owner.male') }}</option><option>{{ t('owner.female') }}</option></select>
          </div>
          <div class="form-group"><label>{{ t('owner.phone') }}</label><input v-model="form.phone" class="input" maxlength="11" /></div>
          <div class="form-group"><label>{{ t('owner.unitSection') }}</label><input v-model="form.unitSection" class="input" maxlength="30" placeholder="1" /></div>
          <div class="form-group"><label>{{ t('owner.propertyCert') }}</label><input v-model="form.propertyCert" class="input" maxlength="30" /></div>
          <div class="form-group">
            <label>{{ t('owner.room') }}</label>
            <select v-model="form.roomId" class="select">
              <option :value="null">-</option>
              <option v-for="u in units" :key="u.id" :value="u.id">{{ unitOptionLabel(u) }}</option>
            </select>
          </div>
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
import { onMounted, reactive, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'
import { showConfirm } from '../composables/useConfirm'

const { t } = useI18n()
const list = ref([])
const units = ref([])
const buildings = ref([])
const keyword = ref('')
const showModal = ref(false)
const formError = ref('')
const form = reactive({ id: null, ownerNo: '', name: '', gender: '男', phone: '', unitSection: '', propertyCert: '', roomId: null, buildingId: null, status: 1 })

function unitOptionLabel(u) {
  const b = buildings.value.find(x => x.id === u.buildingId)
  return `${b?.buildingName || ''} ${u.unitNo}`.trim()
}

function unitLabel(roomId) {
  if (!roomId) return '—'
  const u = units.value.find(x => x.id === roomId)
  return u ? unitOptionLabel(u) : `#${roomId}`
}

async function loadMeta() {
  buildings.value = await request.get('/buildings')
  units.value = await request.get('/units')
}

async function load() {
  list.value = await request.get('/owners', { params: { keyword: keyword.value || undefined } })
}

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, ownerNo: '', name: '', gender: '男', phone: '', unitSection: '', propertyCert: '', roomId: null, buildingId: null, status: 1 })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  if (!form.ownerNo?.trim() || !form.name?.trim()) { formError.value = t('validation.ownerNoRequired'); return }
  const unit = units.value.find(u => u.id === form.roomId)
  if (unit) form.buildingId = unit.buildingId
  try {
    if (form.id) await request.put(`/owners/${form.id}`, form)
    else await request.post('/owners', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteOwner'))
  if (!ok) return
  try { await request.delete(`/owners/${item.id}`); load() } catch (e) { formError.value = e.message }
}

onMounted(async () => { await loadMeta(); load() })
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.actions { display: flex; gap: 8px; }
.search { width: 200px; }
.table .btn { margin-right: 8px; padding: 4px 10px; font-size: 13px; }
.empty { text-align: center; color: var(--color-text-secondary); padding: 24px !important; }
.err { color: var(--color-danger); font-size: 13px; }
</style>
