<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('building.title') }}</h2>
      <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}{{ t('building.name') }}</button>
    </div>
    <div class="card">
      <table class="table">
        <thead>
          <tr><th>{{ t('building.name') }}</th><th>{{ t('building.code') }}</th><th>{{ t('building.floors') }}</th><th>{{ t('building.type') }}</th><th>{{ t('building.address') }}</th><th>{{ t('common.operation') }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.buildingName }}</td>
            <td>{{ item.buildingCode }}</td>
            <td>{{ item.floors ?? '—' }}</td>
            <td>{{ item.buildingType || '—' }}</td>
            <td>{{ item.address || '—' }}</td>
            <td>
              <button class="btn btn-ghost" @click="openForm(item)">{{ t('common.edit') }}</button>
              <button class="btn btn-danger" @click="remove(item)">{{ t('common.delete') }}</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="6" class="empty">{{ t('common.empty') }}</td></tr>
        </tbody>
      </table>
    </div>
    <div v-if="showModal" class="modal-mask" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ form.id ? t('common.edit') : t('common.add') }}{{ t('building.name') }}</h3>
        <div class="form-group"><label>{{ t('building.name') }} *</label><input v-model="form.buildingName" class="input" maxlength="50" /></div>
        <div class="form-group"><label>{{ t('building.code') }} *</label><input v-model="form.buildingCode" class="input" maxlength="20" :readonly="!!form.id" /></div>
        <div class="form-group"><label>{{ t('building.floors') }}</label><input v-model.number="form.floors" type="number" min="1" class="input" /></div>
        <div class="form-group">
          <label>{{ t('building.type') }}</label>
          <select v-model="form.buildingType" class="select"><option>{{ t('building.typeName.住宅') }}</option><option>{{ t('building.typeName.商业') }}</option><option>{{ t('building.typeName.混合') }}</option></select>
        </div>
        <div class="form-group"><label>{{ t('building.address') }}</label><input v-model="form.address" class="input" maxlength="200" /></div>
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
const showModal = ref(false)
const formError = ref('')
const form = reactive({ id: null, buildingName: '', buildingCode: '', floors: 18, buildingType: '住宅', address: '', description: '', sortOrder: 0 })

async function load() { list.value = await request.get('/buildings') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, buildingName: '', buildingCode: '', floors: 18, buildingType: '住宅', address: '', description: '', sortOrder: 0 })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  if (!form.buildingName?.trim() || !form.buildingCode?.trim()) { formError.value = t('validation.nameRequired'); return }
  formError.value = ''
  try {
    if (form.id) await request.put(`/buildings/${form.id}`, form)
    else await request.post('/buildings', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteBuilding'))
  if (!ok) return
  try { await request.delete(`/buildings/${item.id}`); load() } catch (e) { formError.value = e.message }
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.table .btn { margin-right: 8px; padding: 4px 10px; font-size: 13px; }
.empty { text-align: center; color: var(--color-text-secondary); padding: 24px !important; }
.err { color: var(--color-danger); font-size: 13px; }
</style>
