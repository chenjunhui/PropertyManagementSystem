<template>
  <div>
    <h2 class="page-title">{{ t('repair.title') }}</h2>
    <div class="card form-card">
      <div class="form-group"><label>{{ t('repair.title2') }} *</label><input v-model="form.title" class="input" maxlength="100" :placeholder="t('repair.titlePlaceholder')" /></div>
      <div class="form-group"><label>{{ t('repair.description') }}</label><textarea v-model="form.description" class="input textarea" maxlength="500" rows="4" :placeholder="t('repair.descPlaceholder')"></textarea></div>
      <p v-if="error" class="err">{{ error }}</p>
      <button class="btn btn-primary btn-block" :disabled="submitting" @click="submit">{{ t('repair.submit') }}</button>
    </div>
    <div v-if="list.length" class="card" style="margin-top:16px">
      <h3 class="sub-title">{{ t('repair.myRepairs') }}</h3>
      <ul class="history">
        <li v-for="item in list" :key="item.id">
          <strong>{{ item.title }}</strong>
          <span>{{ item.submitDate }} · {{ t(`status.${item.status}`) }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '../api/request'
import { useUserStore } from '../stores/user'

const { t } = useI18n()
const userStore = useUserStore()
const list = ref([])
const error = ref('')
const submitting = ref(false)
const form = reactive({ title: '', description: '' })

async function load() {
  const id = userStore.user?.ownerId
  if (!id) return
  list.value = await request.get(`/repairs/owner/${id}`)
}

async function submit() {
  error.value = ''
  const ownerId = userStore.user?.ownerId
  if (!ownerId) { error.value = t('entity.owner.room_not_found'); return }
  if (!form.title?.trim()) { error.value = t('repair.titleRequired'); return }
  submitting.value = true
  try {
    const owner = await request.get(`/owners/${ownerId}`)
    if (!owner.roomId) { error.value = t('entity.owner.room_not_found'); return }
    await request.post('/repairs', {
      ownerId,
      roomId: owner.roomId,
      title: form.title.trim(),
      description: form.description?.trim() || null
    })
    form.title = ''
    form.description = ''
    await load()
  } catch (e) { error.value = e.message } finally { submitting.value = false }
}

onMounted(load)
</script>

<style scoped>
.form-card { padding: 20px; }
.textarea { resize: vertical; min-height: 96px; }
.btn-block { width: 100%; margin-top: 8px; }
.sub-title { font-size: 15px; font-weight: 600; margin-bottom: 12px; }
.history { list-style: none; display: flex; flex-direction: column; gap: 12px; }
.history li { display: flex; flex-direction: column; gap: 4px; font-size: 14px; }
.history span { color: var(--color-text-secondary); font-size: 13px; }
.err { color: var(--color-danger); font-size: 13px; margin-bottom: 8px; }
</style>
