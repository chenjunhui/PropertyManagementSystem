<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">请假管理</h2>
      <button class="btn btn-primary" @click="openForm()">新增</button>
    </div>
    <div class="card">
      <table class="table">
        <thead><tr><th>学生ID</th><th>类型</th><th>开始</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.studentId }}</td><td>{{ item.leaveType }}</td><td>{{ item.startDate }}</td><td>{{ item.status }}</td>
            <td>
              <button class="btn btn-ghost" @click="openForm(item)">编辑</button>
              <button class="btn btn-danger" @click="remove(item)">删除</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="5" class="empty">暂无数据</td></tr>
        </tbody>
      </table>
    </div>
    <div v-if="showModal" class="modal-mask" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ form.id ? '编辑' : '新增' }}</h3>
        <div class="form-group"><label>学生ID *</label><input v-model.number="form.studentId" type="number" class="input" /></div>
<div class="form-group"><label>类型</label><input v-model="form.leaveType" class="input" /></div>
<div class="form-group"><label>开始</label><input v-model="form.startDate" type="date" class="input" /></div>
<div class="form-group"><label>结束</label><input v-model="form.endDate" type="date" class="input" /></div>
<div class="form-group"><label>原因</label><input v-model="form.reason" class="input" /></div>
<div class="form-group"><label>状态</label><select v-model="form.status" class="select"><option value="PENDING">待审</option><option value="APPROVED">通过</option><option value="REJECTED">拒绝</option></select></div>

        <p v-if="formError" class="err">{{ formError }}</p>
        <div class="modal-actions">
          <button class="btn btn-ghost" @click="showModal = false">取消</button>
          <button class="btn btn-primary" @click="save">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import request from '../api/request'
import { showConfirm } from '../composables/useConfirm'

const list = ref([])
const showModal = ref(false)
const formError = ref('')
const form = reactive({ id: null, studentId: 1, leaveType: "\u4e8b\u5047", startDate: "", endDate: "", reason: "", status: "PENDING" })

async function load() { list.value = await request.get('/leave-requests') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, studentId: 1, leaveType: "\u4e8b\u5047", startDate: "", endDate: "", reason: "", status: "PENDING" })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    if (form.id) await request.put(`/leave-requests/${form.id}`, form)
    else await request.post('/leave-requests', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm('删除确认', '确定删除该记录吗？')
  if (!ok) return
  await request.delete(`/leave-requests/${item.id}`)
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
