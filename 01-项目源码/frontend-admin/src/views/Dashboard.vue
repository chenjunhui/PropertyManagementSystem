<template>
  <div>
    <h2 class="page-title">{{ t('dashboard.title') }}</h2>
    <div class="stats-grid">
      <div v-for="item in statCards" :key="item.key" class="stat-card card">
        <div class="stat-icon" :style="{ background: item.bg }" v-html="item.icon"></div>
        <div>
          <div class="stat-value">{{ item.value }}</div>
          <div class="stat-label">{{ t(`dashboard.${item.key}`) }}</div>
        </div>
      </div>
    </div>
    <div class="charts-grid">
      <div class="card chart-card">
        <h3 class="chart-title">{{ t('dashboard.buildingDistribution') }}</h3>
        <div v-if="buildingData.length" class="bar-chart">
          <div v-for="item in buildingData" :key="item.name" class="bar-row">
            <span class="bar-label">{{ item.name }}</span>
            <div class="bar-track"><div class="bar-fill" :style="{ width: barWidth(item.value, buildingMax) + '%' }"></div></div>
            <span class="bar-value">{{ item.value }}</span>
          </div>
        </div>
        <p v-else class="empty">{{ t('common.empty') }}</p>
      </div>
      <div class="card chart-card">
        <h3 class="chart-title">{{ t('dashboard.repairStatus') }}</h3>
        <ul v-if="repairData.length" class="legend">
          <li v-for="item in repairData" :key="item.name">{{ t(`status.${item.name}`) }} · {{ item.value }}</li>
        </ul>
        <p v-else class="empty">{{ t('common.empty') }}</p>
      </div>
      <div class="card chart-card">
        <h3 class="chart-title">{{ t('dashboard.unitStatus') }}</h3>
        <ul v-if="unitStatusData.length" class="legend">
          <li v-for="item in unitStatusData" :key="item.name">{{ t(`unitStatus.${item.name}`) }} · {{ item.value }}</li>
        </ul>
        <p v-else class="empty">{{ t('common.empty') }}</p>
      </div>
      <div class="card chart-card">
        <h3 class="chart-title">{{ t('dashboard.feePayStatus') }}</h3>
        <ul v-if="feeData.length" class="legend">
          <li v-for="item in feeData" :key="item.name">{{ t(`status.${item.name}`) }} · {{ item.value }}</li>
        </ul>
        <p v-else class="empty">{{ t('common.empty') }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'

const { t } = useI18n()

const statCards = ref([
  { key: 'buildingCount', value: 0, bg: 'var(--color-primary-bg)', icon: '🏢' },
  { key: 'unitCount', value: 0, bg: 'var(--color-primary-bg-deep)', icon: '🏠' },
  { key: 'ownerCount', value: 0, bg: 'rgba(59,130,246,0.12)', icon: '👥' },
  { key: 'pendingRepair', value: 0, bg: 'rgba(217,119,6,0.12)', icon: '🔧' },
  { key: 'unpaidFee', value: 0, bg: 'rgba(239,68,68,0.1)', icon: '💰' },
  { key: 'pendingVisitor', value: 0, bg: 'rgba(139,92,246,0.1)', icon: '🚪' }
])
const buildingData = ref([])
const repairData = ref([])
const unitStatusData = ref([])
const feeData = ref([])
const buildingMax = computed(() => Math.max(...buildingData.value.map(d => d.value), 1))

function barWidth(value, max) { return Math.round((value / max) * 100) }

onMounted(async () => {
  const data = await request.get('/dashboard/stats')
  const pendingRepair = (data.repairStatusDistribution || [])
    .filter(i => i.name === 'PENDING' || i.name === 'PROCESSING')
    .reduce((s, i) => s + i.value, 0)
  const values = [data.buildingCount, data.unitCount, data.ownerCount, pendingRepair, data.unpaidFeeCount, data.pendingVisitorCount]
  statCards.value.forEach((c, i) => { c.value = values[i] ?? 0 })
  buildingData.value = data.buildingDistribution || []
  repairData.value = data.repairStatusDistribution || []
  unitStatusData.value = data.unitStatusDistribution || []
  feeData.value = data.feePayStatusDistribution || []
})
</script>

<style scoped>
.stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 16px; margin-bottom: 24px; }
.stat-card { display: flex; align-items: center; gap: 16px; }
.stat-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 22px; }
.stat-value { font-size: 26px; font-weight: 600; }
.stat-label { font-size: 13px; color: var(--color-text-secondary); }
.charts-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 16px; }
.chart-card { padding: 20px; }
.chart-title { font-size: 15px; font-weight: 600; margin-bottom: 16px; }
.bar-chart { display: flex; flex-direction: column; gap: 12px; }
.bar-row { display: grid; grid-template-columns: 88px 1fr 32px; gap: 10px; align-items: center; }
.bar-track { height: 10px; background: var(--color-primary-bg); border-radius: 5px; overflow: hidden; }
.bar-fill { height: 100%; background: linear-gradient(90deg, var(--color-primary), var(--color-primary-light)); border-radius: 5px; }
.legend { list-style: none; display: flex; flex-direction: column; gap: 8px; font-size: 13px; color: var(--color-text-secondary); }
.empty { color: var(--color-text-secondary); text-align: center; padding: 24px; }
</style>
