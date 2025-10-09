import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Album, CreateAlbumRequest, AlbumPageParams } from '@/types/album'
import { albumApi } from '@/api'

export const useAlbumStore = defineStore('album', () => {
  // State
  const albums = ref<Album[]>([])
  const currentAlbum = ref<Album | null>(null)
  const currentPage = ref(1)
  const pageSize = ref(20)
  const total = ref(0)
  const loading = ref(false)

  // Actions
  const fetchAlbums = async (params: AlbumPageParams = {}) => {
    loading.value = true
    try {
      const res = await albumApi.getAlbumPage({
        current: params.current || currentPage.value,
        size: params.size || pageSize.value,
        categoryId: params.categoryId,
        keyword: params.keyword
      })
      albums.value = res.data.records
      total.value = res.data.total
      currentPage.value = res.data.current
      return res
    } finally {
      loading.value = false
    }
  }

  const fetchStaredAlbums = async (params: { current?: number; size?: number } = {}) => {
    loading.value = true
    try {
      const res = await albumApi.getStaredAlbumPage({
        current: params.current || currentPage.value,
        size: params.size || pageSize.value
      })
      albums.value = res.data.records
      total.value = res.data.total
      currentPage.value = res.data.current
      return res
    } finally {
      loading.value = false
    }
  }

  const fetchAlbumDetail = async (albumId: number) => {
    loading.value = true
    try {
      const res = await albumApi.getAlbumDetail(albumId)
      currentAlbum.value = res.data
      return res
    } finally {
      loading.value = false
    }
  }

  const createAlbum = async (data: CreateAlbumRequest) => {
    loading.value = true
    try {
      const res = await albumApi.createAlbum(data)
      // Add new album to the beginning of the list
      albums.value.unshift(res.data)
      total.value++
      return res
    } finally {
      loading.value = false
    }
  }

  const addImagesToAlbum = async (albumId: number, imageIds: number[]) => {
    try {
      await albumApi.addImageToAlbum(albumId, imageIds)
      // Refresh album detail if current album matches
      if (currentAlbum.value?.id === albumId) {
        await fetchAlbumDetail(albumId)
      }
    } catch (error) {
      console.error('Failed to add images to album:', error)
      throw error
    }
  }

  const removeImageFromAlbum = async (albumId: number, imageId: number) => {
    try {
      await albumApi.removeImageFromAlbum(albumId, imageId)
      // Update local state
      if (currentAlbum.value?.id === albumId && currentAlbum.value.images) {
        const index = currentAlbum.value.images.findIndex(img => img.id === imageId)
        if (index !== -1) {
          currentAlbum.value.images.splice(index, 1)
          currentAlbum.value.imageCount--
        }
      }
    } catch (error) {
      console.error('Failed to remove image from album:', error)
      throw error
    }
  }

  const starAlbum = async (albumId: number) => {
    try {
      await albumApi.starAlbum(albumId)
      // Update local state
      const album = albums.value.find(a => a.id === albumId)
      if (album) {
        album.isStared = true
        album.starCount++
      }
      if (currentAlbum.value?.id === albumId) {
        currentAlbum.value.isStared = true
        currentAlbum.value.starCount++
      }
    } catch (error) {
      console.error('Failed to star album:', error)
      throw error
    }
  }

  const unstarAlbum = async (albumId: number) => {
    try {
      await albumApi.unstarAlbum(albumId)
      // Update local state
      const album = albums.value.find(a => a.id === albumId)
      if (album) {
        album.isStared = false
        album.starCount--
      }
      if (currentAlbum.value?.id === albumId) {
        currentAlbum.value.isStared = false
        currentAlbum.value.starCount--
      }
    } catch (error) {
      console.error('Failed to unstar album:', error)
      throw error
    }
  }

  const deleteAlbum = async (albumId: number) => {
    try {
      await albumApi.deleteAlbum(albumId)
      // Remove from local state
      const index = albums.value.findIndex(a => a.id === albumId)
      if (index !== -1) {
        albums.value.splice(index, 1)
        total.value--
      }
      if (currentAlbum.value?.id === albumId) {
        currentAlbum.value = null
      }
    } catch (error) {
      console.error('Failed to delete album:', error)
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
    albums.value = []
    currentAlbum.value = null
    currentPage.value = 1
    total.value = 0
  }

  return {
    albums,
    currentAlbum,
    currentPage,
    pageSize,
    total,
    loading,
    fetchAlbums,
    fetchStaredAlbums,
    fetchAlbumDetail,
    createAlbum,
    addImagesToAlbum,
    removeImageFromAlbum,
    starAlbum,
    unstarAlbum,
    deleteAlbum,
    setPage,
    setPageSize,
    reset
  }
})
