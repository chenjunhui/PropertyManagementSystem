<template>
  <div class="avatar-upload">
    <img :src="previewUrl" class="avatar-preview" :alt="t('profile.avatar')" />
    <div class="avatar-actions">
      <label class="upload-btn" :class="{ disabled: uploading }">
        {{ uploading ? t('profile.uploading') : (modelValue ? t('profile.changeAvatar') : t('profile.uploadAvatar')) }}
        <input type="file" accept="image/jpeg,image/png,image/gif,image/webp,image/svg+xml" hidden @change="onFileChange" :disabled="uploading" />
      </label>
    </div>
    <p v-if="hint" class="avatar-hint">{{ hint }}</p>
    <p v-if="error" class="upload-error">{{ error }}</p>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { uploadImage } from '../api/upload'
import { resolveAvatarUrl } from '../utils/avatar'

const { t } = useI18n()
const props = defineProps({
  modelValue: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])

const uploading = ref(false)
const error = ref('')

const previewUrl = computed(() => resolveAvatarUrl(props.modelValue))
const hint = computed(() =>
  props.modelValue ? t('profile.changeAvatar') : t('profile.uploadAvatar')
)

async function onFileChange(e) {
  const file = e.target.files?.[0]
  e.target.value = ''
  if (!file) return
  if (file.size > 2 * 1024 * 1024) {
    error.value = t('upload.size_exceeded')
    return
  }
  uploading.value = true
  error.value = ''
  try {
    const url = await uploadImage(file)
    emit('update:modelValue', url)
  } catch (err) {
    error.value = err.message || t('upload.save_failed')
  } finally {
    uploading.value = false
  }
}
</script>

<style scoped>
.avatar-upload { text-align: center; margin-bottom: 24px; }
.avatar-preview {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid var(--color-border, #E2E8F0);
  display: block;
  margin: 0 auto 12px;
  background: #F8FAFC;
}
.avatar-hint {
  font-size: 12px;
  color: var(--color-text-secondary, #64748B);
  margin-top: 4px;
}
.avatar-actions { display: flex; justify-content: center; gap: 12px; flex-wrap: wrap; }
.upload-btn {
  display: inline-block;
  padding: 6px 16px;
  font-size: 13px;
  color: var(--color-primary, #1E40AF);
  background: rgba(30, 64, 175, 0.06);
  border: 1px solid rgba(30, 64, 175, 0.2);
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
}
.upload-btn:hover:not(.disabled) { background: rgba(30, 64, 175, 0.12); }
.upload-btn.disabled { opacity: 0.6; cursor: not-allowed; }
.upload-error { color: #DC2626; font-size: 12px; margin-top: 8px; }
</style>
