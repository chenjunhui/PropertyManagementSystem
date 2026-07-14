<template>

  <div>

    <div class="toolbar">

      <h2 class="page-title">{{ t('nav.roles') || 'Roles' }}</h2>

      <button class="btn btn-primary" @click="openForm()">{{ t('common.add') }}</button>

    </div>

    <div class="card">

      <table class="table">

        <thead>

          <tr><th>{{ t('building.code') }}</th><th>{{ t('owner.name') }}</th><th>{{ t('building.description') }}</th><th>{{ t('common.status') }}</th><th>{{ t('common.operation') }}</th></tr>

        </thead>

        <tbody>

          <tr v-for="item in list" :key="item.id">

            <td>{{ item.roleCode }}</td>

            <td>{{ item.roleName }}</td>

            <td>{{ item.description || '—' }}</td>

            <td>{{ item.status === 1 ? t('common.enable') : t('common.disable') }}</td>

            <td>

              <button class="btn btn-ghost" @click="openForm(item)">{{ t('common.edit') }}</button>

              <button class="btn btn-ghost" @click="openMenus(item)">{{ t('common.edit') }}</button>

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

        <div class="form-group">

          <label>{{ t('building.code') }} <span class="req">*</span></label>

          <input v-model="form.roleCode" class="input" maxlength="20" :readonly="!!form.id" />

        </div>

        <div class="form-group">

          <label>{{ t('owner.name') }} <span class="req">*</span></label>

          <input v-model="form.roleName" class="input" maxlength="50" />

        </div>

        <div class="form-group">

          <label>{{ t('building.description') }}</label>

          <input v-model="form.description" class="input" maxlength="200" />

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



    <div v-if="showMenuModal" class="modal-mask" @click.self="showMenuModal = false">

      <div class="modal modal-lg">

        <h3>{{ t('common.edit') }} · {{ currentRole?.roleName }}</h3>

        <div class="menu-checks">

          <label v-for="m in allMenus" :key="m.id" class="check-item">

            <input type="checkbox" :value="m.id" v-model="selectedMenuIds" />

            {{ m.title }} ({{ m.path }})

          </label>

        </div>

        <div class="modal-actions">

          <button class="btn btn-ghost" @click="showMenuModal = false">{{ t('common.cancel') }}</button>

          <button class="btn btn-primary" @click="saveMenus">{{ t('common.save') }}</button>

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

const allMenus = ref([])

const showModal = ref(false)

const showMenuModal = ref(false)

const formError = ref('')

const currentRole = ref(null)

const selectedMenuIds = ref([])

const form = reactive({ id: null, roleCode: '', roleName: '', description: '', status: 1 })



async function load() {

  list.value = await request.get('/roles')

}



async function loadMenus() {

  allMenus.value = await request.get('/menus')

}



function openForm(item) {

  formError.value = ''

  Object.assign(form, { id: null, roleCode: '', roleName: '', description: '', status: 1 })

  if (item) Object.assign(form, item)

  showModal.value = true

}



async function openMenus(item) {

  currentRole.value = item

  selectedMenuIds.value = await request.get(`/roles/${item.id}/menus`)

  showMenuModal.value = true

}



async function save() {

  formError.value = ''

  if (!form.roleCode?.trim() || !form.roleName?.trim()) {

    formError.value = t('validation.role.code_name_required')

    return

  }

  if (form.id) await request.put(`/roles/${form.id}`, form)

  else await request.post('/roles', form)

  showModal.value = false

  await load()

}



async function saveMenus() {

  await request.put(`/roles/${currentRole.value.id}/menus`, { menuIds: selectedMenuIds.value })

  showMenuModal.value = false

}



async function remove(item) {

  if (!await showConfirm(t('confirm.deleteTitle'), t('confirm.deleteAnnouncement'))) return

  await request.delete(`/roles/${item.id}`)

  await load()

}



onMounted(async () => {

  await load()

  await loadMenus()

})

</script>



<style scoped>

.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }

.page-title { font-size: 20px; font-weight: 600; }

.menu-checks { display: flex; flex-direction: column; gap: 10px; max-height: 320px; overflow-y: auto; margin: 16px 0; }

.check-item { display: flex; align-items: center; gap: 8px; font-size: 14px; cursor: pointer; }

.req { color: #DC2626; }

.err { color: #DC2626; font-size: 13px; }

.empty { text-align: center; color: var(--color-text-secondary); padding: 24px; }

</style>

