import request from './request'
import type { Album, CreateAlbumRequest, AlbumPageParams } from '@/types/album'
import type { PageResult } from '@/types/common'

// Album API
export const albumApi = {
  // Create album
  createAlbum(data: CreateAlbumRequest) {
    return request.post<Album>('/album', data)
  },

  // Get album page
  getAlbumPage(params: AlbumPageParams) {
    return request.get<PageResult<Album>>('/album/page', { params })
  },

  // Get starred album page
  getStaredAlbumPage(params: { current?: number; size?: number }) {
    return request.get<PageResult<Album>>('/album/stared/page', { params })
  },

  // Get album detail
  getAlbumDetail(albumId: number) {
    return request.get<Album>(`/album/${albumId}`)
  },

  // Add images to album
  addImageToAlbum(albumId: number, imageIds: number[]) {
    return request.post<void>(`/album/${albumId}/images`, imageIds)
  },

  // Remove image from album
  removeImageFromAlbum(albumId: number, imageId: number) {
    return request.delete<void>(`/album/${albumId}/images/${imageId}`)
  },

  // Star album
  starAlbum(albumId: number) {
    return request.post<void>(`/album/${albumId}/star`)
  },

  // Unstar album
  unstarAlbum(albumId: number) {
    return request.delete<void>(`/album/${albumId}/star`)
  },

  // Delete album
  deleteAlbum(albumId: number) {
    return request.delete<void>(`/album/${albumId}`)
  }
}
