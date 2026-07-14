<template>
  <div>
    <h2 class="page-title">{{ t('profile.title') }}</h2>
    <div class="card profile-card" v-if="owner">
      <AvatarUpload v-model="form.avatar" />
      <div class="form-grid">
        <div class="form-group"><label>{{ t('owner.ownerNo') }}</label><input :value="owner.ownerNo" class="input" disabled /></div>
        <div class="form-group"><label>{{ t('profile.name') }}</label><input v-model="form.name" class="input" maxlength="50" /></div>
        <div class="form-group"><label>{{ t('profile.gender') }}</label><input :value="owner.gender" class="input" disabled /></div>
        <div class="form-group"><label>{{ t('owner.unitSection') }}</label><input :value="owner.grade || '—'" class="input" disabled /></div>
        <div class="form-group">
          <label>{{ t('profile.phone') }}</label>
          <input v-model="form.phone" class="input" maxlength="11" />
          <span v-if="errors.phone" class="err">{{ errors.phone }}</span>
        </div>
        <div class="form-group">
          <label>{{ t('profile.email') }}</label>
          <input v-model="form.email" class="input" maxlength="100" />
          <span v-if="errors.email" class="err">{{ errors.email }}</span>
        </div>
      </div>
      <p v-if="msg" :class="msgOk ? 'msg-ok' : 'err'">{{ msg }}</p>
      <div class="actions">
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? t('profile.saving') : t('profile.save') }}</button>
        <button type="button" class="btn btn-ghost logout-btn" @click="logout">{{ t('auth.logout') }}</button>
      </div>
    </div>
    <div v-else class="card"><p>{{ t('common.loading') }}</p></div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../stores/user'
import request from '../api/request'
import AvatarUpload from '../components/AvatarUpload.vue'
import { validatePhone, validateEmail } from '../utils/validators'

const { t } = useI18n()
const userStore = useUserStore()
const router = useRouter()
const owner = ref(null)
const saving = ref(false)
const msg = ref('')
const msgOk = ref(false)
const errors = reactive({ phone: '', email: '' })
const form = reactive({ name: '', phone: '', email: '', avatar: '' })

onMounted(async () => {
  if (!userStore.user?.ownerId) return
  owner.value = await request.get(`/owners/${userStore.user.ownerId}`)
  form.name = owner.value.name || ''
  form.phone = owner.value.phone || ''
  form.email = owner.value.email || ''
  form.avatar = owner.value.avatar || ''
  userStore.patchUser({ name: owner.value.name, avatar: owner.value.avatar || null })
})

async function save() {
  errors.phone = validatePhone(form.phone)
  errors.email = validateEmail(form.email)
  if (errors.phone || errors.email) return
  saving.value = true
  msg.value = ''
  try {
    const payload = { name: form.name, phone: form.phone, email: form.email, avatar: form.avatar?.trim() || null }
    const updated = await request.put(`/profile/owner/${owner.value.id}`, payload)
    owner.value = { ...owner.value, ...updated }
    userStore.patchUser({ name: updated.name, avatar: updated.avatar || null })
    msg.value = t('common.save')
    msgOk.value = true
  } catch (e) { msg.value = e.message || t('common.requestFailed'); msgOk.value = false }
  finally { saving.value = false }
}

function logout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.profile-card { max-width: 720px; }
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 20px; }
.form-group label { display: block; font-size: 13px; color: var(--color-text-secondary); margin-bottom: 6px; }
.err { color: #DC2626; font-size: 12px; }
.msg-ok { color: var(--color-primary); font-size: 13px; margin-bottom: 12px; }
.actions { display: flex; flex-direction: column; gap: 12px; margin-top: 8px; }
.logout-btn { color: var(--color-text-secondary); border: 1px solid var(--color-border); }
@media (max-width: 520px) { .form-grid { grid-template-columns: 1fr; } }
</style>
