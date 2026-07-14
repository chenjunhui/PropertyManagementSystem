import { reactive } from 'vue'

export const confirmState = reactive({
  visible: false,
  title: '确认删除',
  message: '',
  resolve: null
})

export function showConfirm(title, message) {
  return new Promise((resolve) => {
    confirmState.title = title
    confirmState.message = message
    confirmState.visible = true
    confirmState.resolve = resolve
  })
}

export function handleConfirmOk() {
  confirmState.resolve?.(true)
  confirmState.visible = false
}

export function handleConfirmCancel() {
  confirmState.resolve?.(false)
  confirmState.visible = false
}
