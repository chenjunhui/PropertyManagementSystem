<template>
  <div>
    <div class="toolbar">
      <h2 class="page-title">{{ t('nav.menus') || 'Menus' }}</h2>
      <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}</button>
    </div>
    <div class="card">
      <table class="table">
        <thead>
          <tr><th>{{ t('owner.name') }}</th><th>{{ t('unit.unitNo') }}</th><th>{{ t('building.type') }}</th><th>{{ t('building.sortOrder') }}</th><th>{{ t('auth.adminPortal') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.title }}</td>
            <td>{{ item.path }}</td>
            <td>
              <div class="icon-cell">
                <span class="icon-preview" v-html="menuIcon(item.icon)"></span>
                <span class="icon-label">{{ iconLabel(item.icon) }}</span>
              </div>
            </td>
            <td>{{ item.sortOrder ?? 0 }}</td>
            <td>{{ item.portal }}</td>
            <td>{{ item.status === 1 ? t('common.enable') : t('common.disable') }}</td>
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
      <div class="modal modal-lg">
        <h3>{{ form.id ? t('common.edit') : t('common.add') }}</h3>
        <div class="form-group">
          <label>{{ t('owner.name') }} <span class="req">*</span></label>
          <input v-model="form.title" class="input" maxlength="50" />
        </div>
        <div class="form-group">
          <label>{{ t('unit.unitNo') }} <span class="req">*</span></label>
          <input v-model="form.path" class="input" maxlength="100" placeholder="/dashboard" />
        </div>
        <div class="form-group">
          <label>{{ t('building.type') }}</label>
          <div class="icon-picker">
            <button
              v-for="opt in ICON_OPTIONS"
              :key="opt.key"
              type="button"
              class="icon-option"
              :class="{ active: form.icon === opt.key }"
              @click="form.icon = opt.key"
            >
              <span class="icon-preview" v-html="menuIcon(opt.key)"></span>
              <span>{{ opt.label }}</span>
            </button>
          </div>
        </div>
        <div class="form-group">
          <label>{{ t('building.sortOrder') }}</label>
          <input v-model.number="form.sortOrder" type="number" class="input" />
        </div>
        <div class="form-group">
          <label>{{ t('auth.adminPortal') }}</label>
          <select v-model="form.portal" class="select">
            <option value="admin">{{ t('auth.adminPortal') }}</option>
            <option value="client">{{ t('auth.clientPortal') }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ t('common.status') }}</label>
          <select v-model="form.status" class="select">
            <option :value="1">{{ t('common.enable') }}</option>
            <option :value="0">{{ t('common.disable') }}</option>
          </select>
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
import { ICON_OPTIONS, menuIcon, iconLabel } from '../utils/menuIcons'

const { t } = useI18n()
const list = ref([])
const showModal = ref(false)
const formError = ref('')
const form = reactive({
  id: null, title: '', path: '', icon: 'dashboard', sortOrder: 0, portal: 'admin', status: 1, parentId: 0
})

async function load() {
  list.value = await request.get('/menus')
}

function openForm(item) {
  formError.value = ''
  Object.assign(form, { id: null, title: '', path: '', icon: 'dashboard', sortOrder: 0, portal: 'admin', status: 1, parentId: 0 })
  if (item) Object.assign(form, item)
  showModal.value = true
}

async function save() {
  formError.value = ''
  if (!form.title?.trim() || !form.path?.trim()) {
    formError.value = t('validation.menu.name_path_required')
    return
  }
  if (form.id) await request.put(`/menus/${form.id}`, form)
  else await request.post('/menus', form)
  showModal.value = false
  await load()
}

async function remove(item) {
  if (!await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteAnnouncement'))) return
  await request.delete(`/menus/${item.id}`)
  await load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-title { font-size: 20px; font-weight: 600; }
.req { color: #DC2626; }
.err { color: #DC2626; font-size: 13px; }
.empty { text-align: center; color: var(--color-text-secondary); padding: 24px; }
.modal-lg { width: min(560px, 94vw); }
.icon-cell { display: flex; align-items: center; gap: 8px; }
.icon-preview { display: inline-flex; align-items: center; color: var(--color-primary); }
.icon-label { font-size: 13px; color: var(--color-text-secondary); }
.icon-picker {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
}
.icon-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 10px 6px;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  background: #fff;
  cursor: pointer;
  font-size: 11px;
  color: var(--color-text-secondary);
  transition: all 0.15s;
}
.icon-option:hover { border-color: var(--color-primary); color: var(--color-primary); }
.icon-option.active {
  border-color: var(--color-primary);
  background: rgba(30, 64, 175, 0.06);
  color: var(--color-primary);
}
@media (max-width: 520px) {
  .icon-picker { grid-template-columns: repeat(2, 1fr); }
}
</style>
