<template>
  <div>
    <h2 class="page-title">请假申请</h2>
    <div v-if="list.length" class="list">
      <div v-for="item in list" :key="item.id" class="card item-card">
        <h3>{{ item.leaveType || '请假申请' }}</h3>
        <p v-if="item.reason" class="body">原因：{{ item.reason }}</p>
        <p class="meta">
          <span v-if="item.startDate">开始日期：{{ item.startDate }}</span>
          <span v-if="item.endDate">结束日期：{{ item.endDate }}</span>
          <span v-if="item.status">状态：{{ statusText(item.status) }}</span>
        </p>
      </div>
    </div>
    <p v-else class="empty card">暂无请假记录</p>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import request from '../api/request'

const list = ref([])
const STATUS = { PENDING: '待审批', APPROVED: '已通过', REJECTED: '已拒绝' }

function statusText(v) {
  return STATUS[v] || v || '—'
}

onMounted(async () => { list.value = await request.get('/leave-requests') })
</script>

<style scoped>
.item-card { margin-bottom: 12px; }
.item-card h3 { font-size: 15px; margin-bottom: 8px; }
.body { font-size: 14px; color: var(--color-text); line-height: 1.6; margin-bottom: 8px; }
.meta { font-size: 13px; color: var(--color-text-secondary); line-height: 1.5; display: flex; flex-wrap: wrap; gap: 4px 12px; }
</style>
