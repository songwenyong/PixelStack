import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Category, CreateCategoryParams, UpdateCategoryParams } from '@/types/category'
import { categoryApi } from '@/api'

export const useCategoryStore = defineStore('category', () => {
  // State
  const categories = ref<Category[]>([])
  const loading = ref(false)

  // Actions
  const fetchCategoryTree = async () => {
    loading.value = true
    try {
      const res = await categoryApi.getCategoryTree()
      categories.value = res.data
      return res
    } finally {
      loading.value = false
    }
  }

  const createCategory = async (params: CreateCategoryParams) => {
    loading.value = true
    try {
      const res = await categoryApi.createCategory(params)
      // Refresh category tree
      await fetchCategoryTree()
      return res
    } finally {
      loading.value = false
    }
  }

  const updateCategory = async (categoryId: number, params: UpdateCategoryParams) => {
    loading.value = true
    try {
      const res = await categoryApi.updateCategory(categoryId, params)
      // Refresh category tree
      await fetchCategoryTree()
      return res
    } finally {
      loading.value = false
    }
  }

  const deleteCategory = async (categoryId: number) => {
    loading.value = true
    try {
      await categoryApi.deleteCategory(categoryId)
      // Refresh category tree
      await fetchCategoryTree()
    } finally {
      loading.value = false
    }
  }

  const reset = () => {
    categories.value = []
  }

  return {
    categories,
    loading,
    fetchCategoryTree,
    createCategory,
    updateCategory,
    deleteCategory,
    reset
  }
})
