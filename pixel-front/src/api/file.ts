import request from './request'

// File API
export const fileApi = {
  // Upload file
  uploadFile(file: File) {
    const formData = new FormData()
    formData.append('file', file)
    return request.post<{
      fileName: string
      fileUrl: string
      originalName: string
    }>('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // Get file URL
  getFileUrl(fileName: string) {
    return `${import.meta.env.VITE_API_BASE_URL || '/api'}/files/${fileName}`
  }
}
