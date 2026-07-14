/** 全局默认头像（静态资源，位于 public/resources/images/avatars/） */
export const DEFAULT_AVATAR = '/resources/images/avatars/default-avatar.png'

/** 个人资料头像 URL：有上传则用上传图，否则用默认头像 */
export function resolveAvatarUrl(avatar) {
  if (avatar && String(avatar).trim()) {
    const path = String(avatar).trim()
    if (path.startsWith('http://') || path.startsWith('https://')) return path
    if (path.startsWith('/api/files/view/')) return path
    if (path.startsWith('/resources/uploads/')) {
      const name = path.slice('/resources/uploads/'.length)
      return name ? `/api/files/view/${name}` : DEFAULT_AVATAR
    }
    return path.startsWith('/') ? path : `/${path}`
  }
  return DEFAULT_AVATAR
}
