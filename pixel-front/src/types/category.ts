// Category related type definitions

export interface Category {
  id: number
  categoryName: string
  parentId?: number
  userId: number
  createTime: string
  children?: Category[]
  albumCount?: number
}

export interface CreateCategoryParams {
  categoryName: string
  parentId?: number
}

export interface UpdateCategoryParams {
  categoryName: string
  parentId?: number
}
