// Image related type definitions

export interface ImageInfo {
  id: number
  fileName: string
  filePath: string
  fileUrl: string
  fileSize: number
  fileMd5: string
  title?: string
  width?: number
  height?: number
  uploadUserId: number
  uploadUserName?: string
  uploadTime: string
  isStared: boolean
  starCount: number
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
