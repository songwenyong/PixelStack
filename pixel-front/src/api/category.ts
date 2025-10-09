import request from './request'
import type { Category, CreateCategoryParams } from '@/types/category'

// Category API
export const categoryApi = {
  // Get category tree
  getCategoryTree() {
    return request.get<Category[]>('/category/tree')
  },

  // Create category
  createCategory(params: CreateCategoryParams) {
    return request.post<Category>('/category', null, { params })
  },

  // Delete category
  deleteCategory(categoryId: number) {
    return request.delete<void>(`/category/${categoryId}`)
  }
}
