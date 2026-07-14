import { defineStore } from 'pinia'

import { ref } from 'vue'

import request from '../api/request'



export const useUserStore = defineStore('user', () => {

  const user = ref(JSON.parse(localStorage.getItem('client_user') || 'null'))

  let refreshTimer = null



  function scheduleTokenRefresh(expiresIn) {

    if (refreshTimer) clearTimeout(refreshTimer)

    const refreshAt = (expiresIn * 0.8) * 1000

    refreshTimer = setTimeout(async () => {

      try {

        await refreshToken()

      } catch (e) {

        logout()

      }

    }, refreshAt)

  }



  async function refreshToken() {

    const currentUser = user.value

    if (!currentUser?.refreshToken) return

    const data = await request.post('/auth/refresh', { refreshToken: currentUser.refreshToken })

    user.value = { ...currentUser, token: data.accessToken, refreshToken: data.refreshToken, expiresIn: data.expiresIn }

    localStorage.setItem('client_user', JSON.stringify(user.value))

    scheduleTokenRefresh(data.expiresIn)

  }



  async function login(username, password) {

    const data = await request.post('/auth/login', { username, password }, { params: { portal: 'client' } })

    user.value = data

    localStorage.setItem('client_user', JSON.stringify(data))

    scheduleTokenRefresh(data.expiresIn)

    return data

  }



  async function register(payload) {

    return request.post('/auth/register', payload)

  }



  function logout() {

    user.value = null

    if (refreshTimer) clearTimeout(refreshTimer)

    localStorage.removeItem('client_user')

  }




  function patchUser(partial) {
    if (!user.value) return
    user.value = { ...user.value, ...partial }
    localStorage.setItem('client_user', JSON.stringify(user.value))
  }

  return { user, login, register, logout, patchUser, refreshToken }
})

