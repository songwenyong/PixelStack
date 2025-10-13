import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ImageInfo, ImagePageParams } from '@/types/image'
import type { PageResult } from '@/types/common'
import { imageApi } from '@/api'

export const useImageStore = defineStore('image', () => {
  // State
  const images = ref<ImageInfo[]>([])
  const currentPage = ref(1)
  const pageSize = ref(50)
  const total = ref(0)
  const loading = ref(false)

  // Actions
  const fetchImages = async (params: ImagePageParams = {}) => {
    loading.value = true
    try {
      const res = await imageApi.getImagePage({
        current: params.current || currentPage.value,
        size: params.size || pageSize.value,
        categoryId: params.categoryId,
        keyword: params.keyword
      })
      images.value = res.data.records
      total.value = res.data.total
      currentPage.value = res.data.current
      return res
    } finally {
      loading.value = false
    }
  }

  const fetchStaredImages = async (params: { current?: number; size?: number } = {}) => {
    loading.value = true
    try {
      const res = await imageApi.getStaredImagePage({
        current: params.current || currentPage.value,
        size: params.size || pageSize.value
      })
      images.value = res.data.records
      total.value = res.data.total
      currentPage.value = res.data.current
      return res
    } finally {
      loading.value = false
    }
  }

  const uploadImage = async (file: File, title?: string) => {
    loading.value = true
    try {
      const res = await imageApi.uploadImage(file, title)
      // Add new image to the beginning of the list
      images.value.unshift(res.data)
      total.value++
      return res
    } finally {
      loading.value = false
    }
  }

  const starImage = async (imageId: number) => {
    try {
      await imageApi.starImage(imageId)
      // Update local state
      const image = images.value.find(img => img.id === imageId)
      if (image) {
        image.isStared = true
      }
    } catch (error) {
      console.error('Failed to star image:', error)
      throw error
    }
  }

  const unstarImage = async (imageId: number) => {
    try {
      await imageApi.unstarImage(imageId)
      // Update local state
      const image = images.value.find(img => img.id === imageId)
      if (image) {
        image.isStared = false
      }
    } catch (error) {
      console.error('Failed to unstar image:', error)
      throw error
    }
  }

  const deleteImage = async (imageId: number) => {
    try {
      await imageApi.deleteImage(imageId)
      // Remove from local state
      const index = images.value.findIndex(img => img.id === imageId)
      if (index !== -1) {
        images.value.splice(index, 1)
        total.value--
      }
    } catch (error) {
      console.error('Failed to delete image:', error)
      throw error
    }
  }

  const setPage = (page: number) => {
    currentPage.value = page
  }

  const setPageSize = (size: number) => {
    pageSize.value = size
  }

  const reset = () => {
    images.value = []
    currentPage.value = 1
    total.value = 0
  }

  return {
    images,
    currentPage,
    pageSize,
    total,
    loading,
    fetchImages,
    fetchStaredImages,
    uploadImage,
    starImage,
    unstarImage,
    deleteImage,
    setPage,
    setPageSize,
    reset
  }
})
