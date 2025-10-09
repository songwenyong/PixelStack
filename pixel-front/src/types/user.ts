export interface User {
  id: number
  username: string
  email: string
  nickname?: string
  avatar?: string
  bio?: string
  role?: string
  createTime?: string
  updateTime?: string
}

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: User
}

export interface RegisterParams {
  username: string
  email: string
  password: string
  nickname?: string
}

export interface UserInfoDTO {
  id: number
  username: string
  email: string
  nickname?: string
  avatar?: string
  bio?: string
  createTime: string
  updateTime: string
}
