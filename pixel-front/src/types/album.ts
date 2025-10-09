// Album related type definitions

import type { ImageInfo } from './image'

export interface Album {
  id: number
  albumName: string
  description?: string
  coverImageUrl?: string
  categoryId?: number
  categoryName?: string
  userId: number
  userName?: string
  createTime: string
  updateTime: string
  imageCount: number
  isStared: boolean
  starCount: number
  images?: ImageInfo[]
  tags?: Tag[]
}

export interface Tag {
  id: number
  tagName: string
  createTime: string
}

export interface CreateAlbumRequest {
  albumName: string
  description?: string
  categoryId?: number
  tagIds?: number[]
}

export interface AlbumPageParams {
  current?: number
  size?: number
  categoryId?: number
  keyword?: string
}
