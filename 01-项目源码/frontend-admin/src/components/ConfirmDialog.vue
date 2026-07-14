<template>
  <Teleport to="body">
    <div v-if="confirmState.visible" class="confirm-mask" @click.self="handleConfirmCancel">
      <div class="confirm-dialog" role="dialog">
        <div class="confirm-icon">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
            <line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>
          </svg>
        </div>
        <h3 class="confirm-title">{{ confirmState.title }}</h3>
        <p class="confirm-message">{{ confirmState.message }}</p>
        <div class="confirm-actions">
          <button class="btn btn-ghost" type="button" @click="handleConfirmCancel">{{ t('common.cancel') }}</button>
          <button class="btn btn-danger" type="button" @click="handleConfirmOk">{{ t('common.confirm') }}</button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { confirmState, handleConfirmOk, handleConfirmCancel } from '../composables/useConfirm'
const { t } = useI18n()
</script>

<style scoped>
.confirm-mask {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  backdrop-filter: blur(2px);
}
.confirm-dialog {
  background: #fff;
  border-radius: 12px;
  padding: 28px 32px;
  width: min(400px, 90vw);
  text-align: center;
  box-shadow: 0 20px 50px rgba(15, 23, 42, 0.18);
  animation: pop 0.2s ease;
}
@keyframes pop {
  from { transform: scale(0.95); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}
.confirm-icon {
  width: 56px; height: 56px;
  margin: 0 auto 16px;
  background: #FEF2F2;
  color: #DC2626;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.confirm-title { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
.confirm-message { color: #64748B; font-size: 14px; margin-bottom: 24px; line-height: 1.6; }
.confirm-actions { display: flex; gap: 12px; justify-content: center; }
.confirm-actions .btn { min-width: 100px; padding: 10px 20px; }
</style>
