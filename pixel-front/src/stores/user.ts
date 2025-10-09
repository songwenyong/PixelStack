import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginParams, RegisterParams } from '@/types/user'
import { userApi } from '@/api'

export const useUserStore = defineStore('user', () => {
  // State
  const userInfo = ref<User | null>(null)
  const token = ref<string>('')
  const loading = ref(false)

  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const userName = computed(() => userInfo.value?.username || userInfo.value?.nickname || '')
  const userAvatar = computed(() => userInfo.value?.avatar || '')

  // Actions
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info: User) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const login = async (params: LoginParams) => {
    loading.value = true
    try {
      const res = await userApi.login(params)
      setToken(res.data.token)
      setUserInfo(res.data.user)
      return res
    } finally {
      loading.value = false
    }
  }

  const register = async (params: RegisterParams) => {
    loading.value = true
    try {
      return await userApi.register(params)
    } finally {
      loading.value = false
    }
  }

  const fetchUserInfo = async () => {
    loading.value = true
    try {
      const res = await userApi.getCurrentUser()
      setUserInfo(res.data as any)
      return res
    } finally {
      loading.value = false
    }
  }

  const updateUserInfo = async (data: Partial<User>) => {
    loading.value = true
    try {
      const res = await userApi.updateUserInfo(data)
      setUserInfo(res.data as any)
      return res
    } finally {
      loading.value = false
    }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const initUserData = () => {
    const savedToken = localStorage.getItem('token')
    const savedUserInfo = localStorage.getItem('userInfo')

    if (savedToken) {
      token.value = savedToken
    }

    if (savedUserInfo) {
      try {
        userInfo.value = JSON.parse(savedUserInfo)
      } catch (e) {
        console.error('Failed to parse user info from localStorage', e)
      }
    }
  }

  return {
    userInfo,
    token,
    loading,
    isLoggedIn,
    userName,
    userAvatar,
    setToken,
    setUserInfo,
    login,
    register,
    fetchUserInfo,
    updateUserInfo,
    logout,
    initUserData
  }
})
