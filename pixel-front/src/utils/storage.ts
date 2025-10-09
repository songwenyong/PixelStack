/**
 * Local storage helper
 */
export const storage = {
  get<T = any>(key: string): T | null {
    const value = localStorage.getItem(key)
    if (!value) return null
    try {
      return JSON.parse(value) as T
    } catch {
      return value as T
    }
  },

  set(key: string, value: any): void {
    const data = typeof value === 'string' ? value : JSON.stringify(value)
    localStorage.setItem(key, data)
  },

  remove(key: string): void {
    localStorage.removeItem(key)
  },

  clear(): void {
    localStorage.clear()
  }
}

/**
 * Session storage helper
 */
export const sessionStorage = {
  get<T = any>(key: string): T | null {
    const value = window.sessionStorage.getItem(key)
    if (!value) return null
    try {
      return JSON.parse(value) as T
    } catch {
      return value as T
    }
  },

  set(key: string, value: any): void {
    const data = typeof value === 'string' ? value : JSON.stringify(value)
    window.sessionStorage.setItem(key, data)
  },

  remove(key: string): void {
    window.sessionStorage.removeItem(key)
  },

  clear(): void {
    window.sessionStorage.clear()
  }
}
