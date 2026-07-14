import { defineStore } from 'pinia'

import { ref } from 'vue'

import request from '../api/request'



export const useUserStore = defineStore('user', () => {

  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

  const menus = ref(JSON.parse(localStorage.getItem('admin_menus') || '[]'))

  let refreshTimer = null



  async function fetchMenus(role) {

    const data = await request.get('/menus', { params: { role } })

    menus.value = data

    localStorage.setItem('admin_menus', JSON.stringify(data))

    return data

  }



  function scheduleTokenRefresh(expiresIn) {

    if (refreshTimer) clearTimeout(refreshTimer)

    // Refresh at 80% of expiration time

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

    localStorage.setItem('user', JSON.stringify(user.value))

    scheduleTokenRefresh(data.expiresIn)

  }



  async function login(username, password) {

    const data = await request.post('/auth/login', { username, password }, { params: { portal: 'admin' } })

    user.value = data

    localStorage.setItem('user', JSON.stringify(data))

    await fetchMenus(data.role)

    scheduleTokenRefresh(data.expiresIn)

    return data

  }



  function logout() {

    user.value = null

    menus.value = []

    if (refreshTimer) clearTimeout(refreshTimer)

    localStorage.removeItem('user')

    localStorage.removeItem('admin_menus')

  }



  return { user, menus, login, logout, fetchMenus, refreshToken }

})

