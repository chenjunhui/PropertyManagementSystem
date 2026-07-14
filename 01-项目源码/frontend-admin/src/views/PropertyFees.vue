<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('propertyFee.title') }}</h2>
      <button class="btn btn-primary" @click="openForm()">{{ t('propertyFee.generateBill') }}</button>
    </div>
    <div class="card">
      <table class="table">
        <thead><tr><th>{{ t('propertyFee.room') }}</th><th>{{ t('propertyFee.billMonth') }}</th><th>{{ t('propertyFee.managementFee') }}</th><th>{{ t('propertyFee.publicFee') }}</th><th>{{ t('propertyFee.totalFee') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ unitLabel(item.roomId) }}</td>
            <td>{{ item.billMonth }}</td>
            <td>¥{{ item.managementFee ?? 0 }}</td>
            <td>¥{{ item.publicFee ?? 0 }}</td>
            <td class="amount">¥{{ item.totalFee ?? 0 }}</td>
            <td><span :class="['tag', item.payStatus === 'PAID' ? 'on' : 'warn']">{{ t(`status.${item.payStatus}`) }}</span></td>
            <td>
              <button class="btn btn-ghost" @click="openForm(item)">{{ t('common.edit') }}</button>
              <button class="btn btn-danger" @click="remove(item)">{{ t('common.delete') }}</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="7" class="empty">{{ t('common.empty') }}</td></tr>
        </tbody>
      </table>
    </div>
    <div v-if="showModal" class="modal-mask" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ form.id ? t('propertyFee.editBill') : t('propertyFee.createBill') }}</h3>
        <div class="form-group">
          <label>{{ t('propertyFee.room') }} *</label>
          <select v-model.number="form.roomId" class="select">
            <option v-for="u in units" :key="u.id" :value="u.id">{{ unitOptionLabel(u) }}</option>
          </select>
        </div>
        <div class="form-group"><label>{{ t('propertyFee.billMonth') }} *</label><input v-model="form.billMonth" class="input" placeholder="2025-03" /></div>
        <div class="form-group"><label>{{ t('propertyFee.managementFee') }}</label><input v-model.number="form.managementFee" type="number" min="0" step="0.01" class="input" @input="calcTotal" /></div>
        <div class="form-group"><label>{{ t('propertyFee.publicFee') }}</label><input v-model.number="form.publicFee" type="number" min="0" step="0.01" class="input" @input="calcTotal" /></div>
        <div class="form-group"><label>{{ t('propertyFee.totalFee') }}</label><input v-model.number="form.totalFee" type="number" class="input" readonly /></div>
        <div class="form-group">
          <label>{{ t('propertyFee.payStatus') }}</label>
          <select v-model="form.payStatus" class="select"><option value="UNPAID">{{ t('status.UNPAID') }}</option><option value="PAID">{{ t('status.PAID') }}</option></select>
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
const showModal = ref(false)
const formError = ref('')
const form = reactive({ id: null, roomId: null, billMonth: '2025-03', managementFee: 0, publicFee: 0, totalFee: 0, payStatus: 'UNPAID' })

function payLabel(s) { return t(`status.${s}`) }
function unitOptionLabel(u) {
  const b = buildings.value.find(x => x.id === u.buildingId)
  return `${b?.buildingName || ''} ${u.unitNo}`.trim()
}
function unitLabel(roomId) {
  const u = units.value.find(x => x.id === roomId)
  return u ? unitOptionLabel(u) : `#${roomId}`
}
function calcTotal() {
  form.totalFee = Number(((form.managementFee || 0) + (form.publicFee || 0)).toFixed(2))
}

async function loadMeta() {
  buildings.value = await request.get('/buildings')
  units.value = await request.get('/units')
}

async function load() { list.value = await request.get('/property-fees') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, roomId: units.value[0]?.id ?? null, billMonth: '2025-03', managementFee: 280, publicFee: 120, totalFee: 400, payStatus: 'UNPAID' })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  if (!form.roomId || !form.billMonth?.trim()) { formError.value = t('propertyFee.roomRequired'); return }
  calcTotal()
  try {
    if (form.id) await request.put(`/property-fees/${form.id}`, form)
    else await request.post('/property-fees', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteFee'))
  if (!ok) return
  await request.delete(`/property-fees/${item.id}`)
  load()
}

onMounted(async () => { await loadMeta(); load() })
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.table .btn { margin-right: 8px; padding: 4px 10px; font-size: 13px; }
.amount { font-weight: 600; color: var(--color-primary); }
.tag.warn { background: #FEF3C7; color: #B45309; }
.empty { text-align: center; color: var(--color-text-secondary); padding: 24px !important; }
.err { color: var(--color-danger); font-size: 13px; }
</style>
