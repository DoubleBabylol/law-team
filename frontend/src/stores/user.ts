import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { LoginResponse, Menu, Role } from '@/types'
import { login as loginApi } from '@/api/auth'
import type { LoginRequest } from '@/types'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref('')
  const username = ref('')
  const roles = ref<Role[]>([])
  const menus = ref<Menu[]>([])

  function setLoginInfo(data: LoginResponse) {
    token.value = data.token
    userId.value = data.userId
    username.value = data.username
    roles.value = data.roles
    menus.value = data.menus
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify({ userId: data.userId, username: data.username, roles: data.roles, menus: data.menus }))
  }

  function loadFromStorage() {
    const info = localStorage.getItem('userInfo')
    if (info) {
      const parsed = JSON.parse(info)
      userId.value = parsed.userId
      username.value = parsed.username
      roles.value = parsed.roles || []
      menus.value = parsed.menus || []
    }
  }

  async function login(loginForm: LoginRequest) {
    const res = await loginApi(loginForm)
    setLoginInfo(res.data)
  }

  function logout() {
    token.value = ''
    userId.value = ''
    username.value = ''
    roles.value = []
    menus.value = []
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return { token, userId, username, roles, menus, login, logout, loadFromStorage }
})
