// Common type definitions

export interface ApiResponse<T = any> {
  code: number
  data: T
  message: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

export interface PageParams {
  current?: number
  size?: number
  keyword?: string
}
