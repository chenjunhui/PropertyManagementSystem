import axios from 'axios'
import router from '../router'
import i18n from '../i18n'

const request = axios.create({ baseURL: '/api', timeout: 10000 })

request.interceptors.request.use(
  (config) => {
    const user = JSON.parse(localStorage.getItem('client_user') || 'null')
    if (user?.token) {
      config.headers.Authorization = `Bearer ${user.token}`
    }
    return config
  },
  (err) => Promise.reject(err)
)

request.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body.code !== 200) return Promise.reject(new Error(body.message || i18n.global.t('common.requestFailed')))
    return body.data
  },
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('client_user')
      router.push('/login')
    }
    return Promise.reject(err)
  }
)

export default request
