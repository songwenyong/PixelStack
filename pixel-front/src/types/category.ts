// Category related type definitions

export interface Category {
  id: number
  categoryName: string
  parentId?: number
  userId: number
  createTime: string
  children?: Category[]
}

export interface CreateCategoryParams {
  categoryName: string
  parentId?: number
}
