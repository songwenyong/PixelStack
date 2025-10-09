import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { useUserStore } from '@/stores/user'

// Response data type
export interface ApiResponse<T = any> {
  code: number
  data: T
  message: string
}

class Request {
  private instance: AxiosInstance

  constructor(config: AxiosRequestConfig) {
    this.instance = axios.create(config)

    // Request interceptor
    this.instance.interceptors.request.use(
      config => {
        const userStore = useUserStore()
        if (userStore.token) {
          config.headers.Authorization = `Bearer ${userStore.token}`
        }
        return config
      },
      error => {
        return Promise.reject(error)
      }
    )

    // Response interceptor
    this.instance.interceptors.response.use(
      (response: AxiosResponse) => {
        const res = response.data
        // Handle based on your API response format
        if (res.code !== 200) {
          // Handle error
          console.error(res.message || 'Error')
          return Promise.reject(new Error(res.message || 'Error'))
        }
        return res
      },
      error => {
        // Handle HTTP errors
        if (error.response) {
          switch (error.response.status) {
            case 401:
              // Unauthorized - redirect to login
              const userStore = useUserStore()
              userStore.logout()
              window.location.href = '/login'
              break
            case 403:
              console.error('Access denied')
              break
            case 404:
              console.error('Resource not found')
              break
            case 500:
              console.error('Server error')
              break
            default:
              console.error('Network error')
          }
        }
        return Promise.reject(error)
      }
    )
  }

  // Generic request method
  request<T = any>(config: AxiosRequestConfig): Promise<ApiResponse<T>> {
    return this.instance.request(config)
  }

  // GET request
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    return this.instance.get(url, config)
  }

  // POST request
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    return this.instance.post(url, data, config)
  }

  // PUT request
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    return this.instance.put(url, data, config)
  }

  // DELETE request
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    return this.instance.delete(url, config)
  }
}

// Create axios instance
const request = new Request({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default request
