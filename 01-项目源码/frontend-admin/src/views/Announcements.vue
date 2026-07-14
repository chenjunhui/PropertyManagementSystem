<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('announcement.title') }}</h2>
      <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}</button>
    </div>
    <div class="card">
      <table class="table">
        <thead><tr><th>{{ t('announcement.title') }}</th><th>{{ t('announcement.publishDate') }}</th><th>{{ t('announcement.publisher') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.title }}</td><td>{{ item.publishDate }}</td><td>{{ item.publisher }}</td><td>{{ t(`status.${item.status}`) }}</td>
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
        <div class="form-group"><label>{{ t('announcement.title') }} *</label><input v-model="form.title" class="input" /></div>
<div class="form-group"><label>{{ t('announcement.content') }}</label><input v-model="form.content" class="input" /></div>
<div class="form-group"><label>{{ t('announcement.publishDate') }}</label><input v-model="form.publishDate" type="date" class="input" /></div>
<div class="form-group"><label>{{ t('announcement.publisher') }}</label><input v-model="form.publisher" class="input" /></div>
<div class="form-group"><label>{{ t('common.status') }}</label><select v-model="form.status" class="select"><option value="PUBLISHED">{{ t('status.PUBLISHED') }}</option><option value="DRAFT">{{ t('status.DRAFT') }}</option></select></div>

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
const form = reactive({ id: null, title: "", content: "", publishDate: "", publisher: "", status: "PUBLISHED" })

async function load() { list.value = await request.get('/announcements') }

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, title: "", content: "", publishDate: "", publisher: "", status: "PUBLISHED" })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  formError.value = ''
  try {
    if (form.id) await request.put(`/announcements/${form.id}`, form)
    else await request.post('/announcements', form)
    showModal.value = false
    load()
  } catch (e) { formError.value = e.message }
}

async function remove(item) {
  const ok = await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteAnnouncement'))
  if (!ok) return
  await request.delete(`/announcements/${item.id}`)
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
