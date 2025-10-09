import request from './request'
import type { ImageInfo, ImagePageParams } from '@/types/image'
import type { PageResult } from '@/types/common'

// Image API
export const imageApi = {
  // Upload image
  uploadImage(file: File, title?: string) {
    const formData = new FormData()
    formData.append('file', file)
    if (title) {
      formData.append('title', title)
    }
    return request.post<ImageInfo>('/image/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // Get image page
  getImagePage(params: ImagePageParams) {
    return request.get<PageResult<ImageInfo>>('/image/page', { params })
  },

  // Get starred image page
  getStaredImagePage(params: { current?: number; size?: number }) {
    return request.get<PageResult<ImageInfo>>('/image/stared/page', { params })
  },

  // Star image
  starImage(imageId: number) {
    return request.post<void>(`/image/${imageId}/star`)
  },

  // Unstar image
  unstarImage(imageId: number) {
    return request.delete<void>(`/image/${imageId}/star`)
  },

  // Delete image
  deleteImage(imageId: number) {
    return request.delete<void>(`/image/${imageId}`)
  }
}
