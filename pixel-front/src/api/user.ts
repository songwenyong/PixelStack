import request from './request'
import type { LoginParams, RegisterParams, LoginResponse, UserInfoDTO } from '@/types/user'

// User API
export const userApi = {
  // Login
  login(data: LoginParams) {
    return request.post<LoginResponse>('/user/login', data)
  },

  // Register
  register(data: RegisterParams) {
    return request.post<void>('/user/register', data)
  },

  // Get current user info
  getCurrentUser() {
    return request.get<UserInfoDTO>('/user/current')
  },

  // Update user info
  updateUserInfo(data: Partial<UserInfoDTO>) {
    return request.put<UserInfoDTO>('/user/update', data)
  }
}
