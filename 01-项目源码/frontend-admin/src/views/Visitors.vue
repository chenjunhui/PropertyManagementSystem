<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('visitor.title') }}</h2>
      <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}</button>
    </div>
    <div class="card">
      <table class="table">
        <thead><tr><th>{{ t('visitor.visitorName') }}</th><th>{{ t('visitor.visitorPhone') }}</th><th>{{ t('visitor.visitDate') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.visitorName }}</td><td>{{ item.visitorPhone }}</td><td>{{ item.visitDate }}</td><td>{{ t(`status.${item.status}`) }}</td>
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
        <div class="form-group"><label>Owner ID *</label><input v-model.number="form.ownerId" type="number" class="input" /></div>
<div class="form-group"><label>{{ t('visitor.visitorName') }} *</label><input v-model="form.visitorName" class="input" /></div>
<div class="form-group"><label>{{ t('visitor.visitorPhone') }}</label><input v-model="form.visitorPhone" class="input" /></div>
<div class="form-group"><label>{{ t('visitor.visitDate') }}</label><input v-model="form.visitDate" type="date" class="input" /></div>
<div class="form-group"><label>{{ t('visitor.reason') }}</label><input v-model="form.reason" class="input" /></div>
<div class="form-group"><label>{{ t('common.status') }}</label><select v-model="form.status" class="select"><option value="PENDING">{{ t('status.PENDING') }}</option><option value="APPROVED">{{ t('status.APPROVED') }}</option><option value="REJECTED">{{ t('status.REJECTED') }}</option></select></div>

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
const form = reactive({ id: null, ownerId: 1, visitorName: "", visitorPhone: "", visitDate: "", reason: "", status: "PENDING" })

async function load() { list.value = await request.get('/visitors') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, ownerId: 1, visitorName: "", visitorPhone: "", visitDate: "", reason: "", status: "PENDING" })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    if (form.id) await request.put(`/visitors/${form.id}`, form)
    else await request.post('/visitors', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteVisitor'))
  if (!ok) return
  await request.delete(`/visitors/${item.id}`)
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
