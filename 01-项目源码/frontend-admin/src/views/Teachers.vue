<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">教师管理</h2>
      <button class="btn btn-primary" @click="openForm()">新增</button>
    </div>
    <div class="card">
      <table class="table">
        <thead><tr><th>工号</th><th>姓名</th><th>院系</th><th>职称</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.teacherNo }}</td><td>{{ item.name }}</td><td>{{ item.department }}</td><td>{{ item.title }}</td>
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
        <div class="form-group"><label>工号 *</label><input v-model="form.teacherNo" class="input" /></div>
<div class="form-group"><label>姓名 *</label><input v-model="form.name" class="input" /></div>
<div class="form-group"><label>性别</label><input v-model="form.gender" class="input" /></div>
<div class="form-group"><label>手机</label><input v-model="form.phone" class="input" /></div>
<div class="form-group"><label>邮箱</label><input v-model="form.email" class="input" /></div>
<div class="form-group"><label>院系</label><input v-model="form.department" class="input" /></div>
<div class="form-group"><label>职称</label><input v-model="form.title" class="input" /></div>

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
const form = reactive({ id: null, teacherNo: "", name: "", gender: "\u7537", phone: "", email: "", department: "", title: "" })

async function load() { list.value = await request.get('/teachers') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, teacherNo: "", name: "", gender: "\u7537", phone: "", email: "", department: "", title: "" })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    if (form.id) await request.put(`/teachers/${form.id}`, form)
    else await request.post('/teachers', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm('删除确认', '确定删除该记录吗？')
  if (!ok) return
  await request.delete(`/teachers/${item.id}`)
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
