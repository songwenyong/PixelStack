// Image related type definitions

export interface ImageInfo {
  id: number
  title: string
  fileName: string
  format: string
  url: string
  thumbnailUrl: string
  creator: number
  creatorName?: string
  createdAt: string
  isStared: boolean
}

export interface UploadImageParams {
  file: File
  title?: string
}

export interface ImagePageParams {
  current?: number
  size?: number
  keyword?: string
}
