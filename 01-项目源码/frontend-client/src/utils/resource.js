/** 将头像等资源路径转为可访问 URL（走 /api 代理，避免与 public 静态目录冲突） */
export function resolveResourceUrl(path) {
  if (!path) return ''
  if (path.startsWith('http://') || path.startsWith('https://')) return path
  if (path.startsWith('/api/files/view/')) return path
  if (path.startsWith('/resources/uploads/')) {
    const name = path.slice('/resources/uploads/'.length)
    return name ? `/api/files/view/${name}` : path
  }
  return path.startsWith('/') ? path : `/${path}`
}
