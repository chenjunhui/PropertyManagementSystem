<template>
  <div>
    <h2 class="page-title">{{ t('myProperty.title') }}</h2>
    <div v-if="owner" class="card info-card">
      <div class="property-header">
        <div class="property-icon">🏠</div>
        <div>
          <div class="property-title">{{ buildingName }} · {{ unit?.unitNo || '-' }}</div>
          <div class="property-sub">{{ unit?.unitType || '—' }} · {{ unit?.areaSqm ? unit.areaSqm + t('myProperty.sqm') : '—' }}</div>
        </div>
      </div>
      <div class="row"><span class="label">{{ t('owner.ownerNo') }}</span><span>{{ owner.ownerNo }}</span></div>
      <div class="row"><span class="label">{{ t('owner.name') }}</span><span>{{ owner.name }}</span></div>
      <div class="row"><span class="label">{{ t('owner.propertyCert') }}</span><span>{{ owner.propertyCert || '—' }}</span></div>
      <div class="row"><span class="label">{{ t('owner.unitSection') }}</span><span>{{ owner.unitSection || '—' }}</span></div>
      <div class="row"><span class="label">{{ t('owner.registerDate') }}</span><span>{{ owner.registerDate || '—' }}</span></div>
    </div>
    <div v-if="fees.length" class="card section-card">
      <h3 class="sub-title">{{ t('propertyFee.title') }}</h3>
      <div v-for="item in fees" :key="item.id" class="fee-row">
        <div>
          <div class="fee-month">{{ item.billMonth }}</div>
          <div class="fee-detail">¥{{ item.managementFee }} + ¥{{ item.publicFee }}</div>
        </div>
        <div class="fee-right">
          <span class="fee-amount">¥{{ item.totalFee }}</span>
          <span :class="['fee-tag', item.payStatus === 'PAID' ? 'paid' : 'unpaid']">{{ t(`status.${item.payStatus}`) }}</span>
        </div>
      </div>
    </div>
    <div v-if="repairs.length" class="card section-card">
      <h3 class="sub-title">{{ t('repair.myRepairs') }}</h3>
      <table class="table">
        <thead><tr><th>{{ t('repair.title2') }}</th><th>{{ t('repair.submitDate') }}</th><th>{{ t('common.status') }}</th></tr></thead>
        <tbody>
          <tr v-for="item in repairs" :key="item.id">
            <td>{{ item.title }}</td>
            <td>{{ item.submitDate || '—' }}</td>
            <td>{{ t(`status.${item.status}`) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-if="!owner && !loading" class="empty card">{{ t('empty.noProperty') }}</p>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'

const { t } = useI18n()
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const owner = ref(null)
const unit = ref(null)
const buildingName = ref('')
const fees = ref([])
const repairs = ref([])
const loading = ref(true)

function statusLabel(s) { return t(`status.${s}`) }

onMounted(async () => {
  const ownerId = userStore.user?.ownerId
  if (!ownerId) { loading.value = false; return }
  try {
    owner.value = await request.get(`/owners/${ownerId}`)
    if (owner.value.roomId) {
      unit.value = await request.get(`/units/${owner.value.roomId}`)
      if (unit.value?.buildingId) {
        const b = await request.get(`/buildings/${unit.value.buildingId}`)
        buildingName.value = b.buildingName
      }
      const allFees = await request.get('/property-fees')
      fees.value = allFees.filter(f => f.roomId === owner.value.roomId)
    }
    repairs.value = await request.get(`/repairs/owner/${ownerId}`)
  } finally { loading.value = false }
})
</script>

<style scoped>
.info-card { padding: 20px; }
.property-header { display: flex; gap: 14px; align-items: center; margin-bottom: 16px; padding-bottom: 16px; border-bottom: 1px solid var(--color-border); }
.property-icon { width: 52px; height: 52px; background: var(--color-primary-bg); border-radius: 14px; display: flex; align-items: center; justify-content: center; font-size: 26px; }
.property-title { font-size: 17px; font-weight: 600; }
.property-sub { font-size: 13px; color: var(--color-text-secondary); margin-top: 4px; }
.row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid var(--color-border); font-size: 14px; }
.row:last-child { border-bottom: none; }
.label { color: var(--color-text-secondary); }
.section-card { margin-top: 16px; padding: 20px; }
.sub-title { font-size: 15px; font-weight: 600; margin-bottom: 12px; }
.fee-row { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid var(--color-border); }
.fee-row:last-child { border-bottom: none; }
.fee-month { font-weight: 500; }
.fee-detail { font-size: 12px; color: var(--color-text-secondary); margin-top: 4px; }
.fee-right { text-align: right; }
.fee-amount { display: block; font-weight: 600; color: var(--color-primary); }
.fee-tag { font-size: 12px; padding: 2px 8px; border-radius: 10px; }
.fee-tag.paid { background: var(--color-primary-bg); color: var(--color-primary); }
.fee-tag.unpaid { background: #FEF3C7; color: #B45309; }
.empty { text-align: center; padding: 32px; color: var(--color-text-secondary); }
</style>
