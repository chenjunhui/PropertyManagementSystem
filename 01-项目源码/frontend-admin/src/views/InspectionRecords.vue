<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('inspection.title') }}</h2>
      <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}</button>
    </div>
    <div class="card">
      <table class="table">
        <thead><tr><th>{{ t('inspection.room') }}</th><th>{{ t('inspection.inspector') }}</th><th>{{ t('inspection.score') }}</th><th>{{ t('inspection.result') }}</th><th>{{ t('common.operation') }}</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.roomId }}</td><td>{{ item.inspectorName }}</td><td>{{ item.score }}</td><td>{{ t(`status.${item.result}`) }}</td>
            <td>
              <button class="btn btn-ghost" @click="openForm(item)">{{ t('common.edit') }}</button>
              <button class="btn btn-danger" @click="remove(item)">{{ t('common.delete') }}</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="5" class="empty">{{ t('common.empty') }}</td></tr>
        </tbody>
      </table>
    </div>
    <div v-if="showModal" class="modal-mask" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ form.id ? t('common.edit') : t('common.add') }}</h3>
        <div class="form-group"><label>{{ t('inspection.room') }} *</label><input v-model.number="form.roomId" type="number" class="input" /></div>
<div class="form-group"><label>{{ t('inspection.inspector') }}</label><input v-model="form.inspectorName" class="input" /></div>
<div class="form-group"><label>{{ t('inspection.score') }}</label><input v-model.number="form.score" type="number" class="input" /></div>
<div class="form-group"><label>{{ t('inspection.checkDate') }}</label><input v-model="form.checkDate" type="date" class="input" /></div>
<div class="form-group"><label>{{ t('inspection.issues') }}</label><input v-model="form.issues" class="input" /></div>
<div class="form-group"><label>{{ t('inspection.result') }}</label><select v-model="form.result" class="select"><option value="PASS">{{ t('status.PASS') }}</option><option value="RECTIFY">{{ t('status.RECTIFY') }}</option></select></div>

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
const form = reactive({ id: null, roomId: 1, inspectorName: "", score: 90, checkDate: "", issues: "", result: "PASS" })

async function load() { list.value = await request.get('/inspection-records') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, roomId: 1, inspectorName: "", score: 90, checkDate: "", issues: "", result: "PASS" })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    if (form.id) await request.put(`/inspection-records/${form.id}`, form)
    else await request.post('/inspection-records', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteInspection'))
  if (!ok) return
  await request.delete(`/inspection-records/${item.id}`)
  load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.table .btn { margin-right: 8px; padding: 4px 10px; font-size: 13px; }
.empty { text-align: center; color: var(--color-text-secondary); padding: 24px !important; }
.err { color: var(--color-danger); font-size: 13px; }
</style>
