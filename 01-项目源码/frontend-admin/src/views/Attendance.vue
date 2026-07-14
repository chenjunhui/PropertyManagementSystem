<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">考勤管理</h2>
      <button class="btn btn-primary" @click="openForm()">新增</button>
    </div>
    <div class="card">
      <table class="table">
        <thead><tr><th>学生ID</th><th>课程ID</th><th>日期</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.studentId }}</td><td>{{ item.courseId }}</td><td>{{ item.attendDate }}</td><td>{{ item.status }}</td>
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
<div class="form-group"><label>课程ID *</label><input v-model.number="form.courseId" type="number" class="input" /></div>
<div class="form-group"><label>日期</label><input v-model="form.attendDate" type="date" class="input" /></div>
<div class="form-group"><label>状态</label><select v-model="form.status" class="select"><option value="PRESENT">出勤</option><option value="ABSENT">缺勤</option><option value="LATE">迟到</option></select></div>
<div class="form-group"><label>备注</label><input v-model="form.remark" class="input" /></div>

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
const form = reactive({ id: null, studentId: 1, courseId: 1, attendDate: "", status: "PRESENT", remark: "" })

async function load() { list.value = await request.get('/attendance') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, studentId: 1, courseId: 1, attendDate: "", status: "PRESENT", remark: "" })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    if (form.id) await request.put(`/attendance/${form.id}`, form)
    else await request.post('/attendance', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm('删除确认', '确定删除该记录吗？')
  if (!ok) return
  await request.delete(`/attendance/${item.id}`)
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
