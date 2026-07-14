export function validatePhone(phone, required = false) {
  if (!phone?.trim()) return required ? '手机号不能为空' : ''
  if (phone.length !== 11 || !/^1\d{10}$/.test(phone)) return '手机号必须为11位且以1开头'
  return ''
}

export function validateEmail(email) {
  if (!email?.trim()) return ''
  if (!/^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,}$/.test(email)) return '邮箱格式不正确'
  return ''
}

export function validateOwnerNo(no) {
  if (!no?.trim()) return '业主编号不能为空'
  if (!/^[A-Za-z0-9]{4,20}$/.test(no)) return '业主编号仅支持4-20位字母或数字'
  return ''
}

/** @deprecated 使用 validateOwnerNo */
export const validateStudentNo = validateOwnerNo
