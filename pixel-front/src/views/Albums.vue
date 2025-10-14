<template>
  <div class="albums-page">
    <n-space vertical :size="24">
      <n-space justify="space-between" align="center">
        <h1>{{ $t('albums.myAlbums') }}</h1>
        <n-space>
          <n-input
            v-model:value="searchKeyword"
            :placeholder="$t('albums.searchAlbums')"
            clearable
            style="width: 300px"
            @update:value="handleSearch"
          >
            <template #prefix>
              <n-icon><SearchOutline /></n-icon>
            </template>
          </n-input>

          <n-button type="primary" @click="showCreateModal = true">
            <template #icon>
              <n-icon><AddOutline /></n-icon>
            </template>
            {{ $t('albums.createAlbum') }}
          </n-button>
        </n-space>
      </n-space>

      <n-spin :show="albumStore.loading">
        <n-grid x-gap="16" y-gap="16" :cols="responsiveCols">
          <n-grid-item v-for="album in albumStore.albums" :key="album.id">
            <n-card
              class="album-card"
              :segmented="{ content: 'soft' }"
              hoverable
              @click="handleAlbumClick(album.id)"
            >
              <div class="album-cover">
                <n-image
                  v-if="album.coverImageUrl"
                  :src="album.coverImageUrl"
                  :alt="album.albumName"
                  object-fit="cover"
                  class="cover-image"
                  lazy
                />
                <div v-else class="empty-cover">
                  <n-icon size="48"><AlbumsOutline /></n-icon>
                </div>
              </div>

              <template #footer>
                <n-space vertical :size="8">
                  <n-ellipsis style="font-weight: 600">
                    {{ album.albumName }}
                  </n-ellipsis>

                  <n-ellipsis v-if="album.description" :line-clamp="2">
                    {{ album.description }}
                  </n-ellipsis>

                  <n-space justify="space-between" align="center">
                    <n-text depth="3">
                      {{ album.imageCount }} {{ $t('albums.imagesCount') }}
                    </n-text>

                    <n-space @click.stop>
                      <n-button
                        text
                        @click="handleToggleStar(album)"
                      >
                        <template #icon>
                          <n-icon :color="album.isStared ? '#f59e0b' : undefined">
                            <Star v-if="album.isStared" />
                            <StarOutline v-else />
                          </n-icon>
                        </template>
                        {{ album.starCount }}
                      </n-button>

                      <n-button
                        text
                        type="info"
                        @click="handleEditClick(album.id)"
                      >
                        <template #icon>
                          <n-icon><CreateOutline /></n-icon>
                        </template>
                      </n-button>

                      <n-popconfirm
                        @positive-click="handleDelete(album.id)"
                      >
                        <template #trigger>
                          <n-button text type="error">
                            <template #icon>
                              <n-icon><TrashOutline /></n-icon>
                            </template>
                          </n-button>
                        </template>
                        {{ $t('albums.deleteConfirmText') }}
                      </n-popconfirm>
                    </n-space>
                  </n-space>
                </n-space>
              </template>
            </n-card>
          </n-grid-item>
        </n-grid>

        <n-empty
          v-if="!albumStore.loading && albumStore.albums.length === 0"
          :description="$t('albums.noAlbumsFound')"
          style="margin-top: 60px"
        >
          <template #extra>
            <n-button type="primary" @click="showCreateModal = true">
              {{ $t('albums.createFirstAlbum') }}
            </n-button>
          </template>
        </n-empty>

        <div v-if="albumStore.total > 0" class="pagination-wrapper">
          <n-pagination
            v-model:page="albumStore.currentPage"
            v-model:page-size="albumStore.pageSize"
            :page-count="Math.ceil(albumStore.total / albumStore.pageSize)"
            :item-count="albumStore.total"
            show-size-picker
            show-quick-jumper
            :page-sizes="[20, 50, 100, 200]"
            @update:page="handlePageChange"
            @update:page-size="handlePageSizeChange"
          >
            <template #prefix="{ itemCount }">
              <span class="pagination-info">{{ $t('common.total') }}: {{ itemCount }}</span>
            </template>
          </n-pagination>
        </div>
      </n-spin>
    </n-space>

    <!-- Create Album Modal -->
    <n-modal
      v-model:show="showCreateModal"
      preset="card"
      :title="$t('albums.createAlbum')"
      style="width: 600px"
    >
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="top"
      >
        <n-form-item :label="$t('albums.albumName')" path="albumName">
          <n-input
            v-model:value="formData.albumName"
            :placeholder="$t('albums.enterAlbumName')"
          />
        </n-form-item>

        <n-form-item :label="$t('albums.description')" path="description">
          <n-input
            v-model:value="formData.description"
            type="textarea"
            :placeholder="$t('albums.enterDescription')"
            :rows="3"
          />
        </n-form-item>

        <n-form-item :label="$t('albums.category')" path="categoryId">
          <n-select
            v-model:value="formData.categoryId"
            :options="categoryOptions"
            :placeholder="$t('albums.selectCategory')"
          />
        </n-form-item>

        <n-form-item :label="$t('albums.tags')">
          <n-space vertical style="width: 100%">
            <n-space v-if="formData.tagNames && formData.tagNames.length > 0">
              <n-tag
                v-for="tag in formData.tagNames"
                :key="tag"
                closable
                @close="handleRemoveTag(tag)"
              >
                {{ tag }}
              </n-tag>
            </n-space>
            <n-input-group>
              <n-input
                v-model:value="tagInput"
                :placeholder="$t('albums.enterTag')"
                @keyup.enter="handleAddTag"
              />
              <n-button type="primary" @click="handleAddTag">
                {{ $t('albums.addTag') }}
              </n-button>
            </n-input-group>
          </n-space>
        </n-form-item>
      </n-form>

      <template #footer>
        <n-space justify="end">
          <n-button @click="showCreateModal = false">{{ $t('albums.cancel') }}</n-button>
          <n-button type="primary" :loading="creating" @click="handleCreate">
            {{ $t('albums.create') }}
          </n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- Edit Album Modal -->
    <n-modal
      v-model:show="showEditModal"
      preset="card"
      :title="$t('albums.editAlbum')"
      style="width: 900px"
    >
      <n-spin :show="loadingEditData">
        <n-form
          ref="editFormRef"
          :model="editFormData"
          :rules="editRules"
          label-placement="top"
        >
          <n-form-item :label="$t('albums.albumName')" path="albumName">
            <n-input
              v-model:value="editFormData.albumName"
              :placeholder="$t('albums.enterAlbumName')"
            />
          </n-form-item>

          <n-form-item :label="$t('albums.description')" path="description">
            <n-input
              v-model:value="editFormData.description"
              type="textarea"
              :placeholder="$t('albums.enterDescription')"
              :rows="3"
            />
          </n-form-item>

          <n-form-item :label="$t('albums.category')" path="categoryId">
            <n-select
              v-model:value="editFormData.categoryId"
              :options="categoryOptions"
              :placeholder="$t('albums.selectCategory')"
            />
          </n-form-item>

          <n-form-item :label="$t('albums.tags')">
            <n-space vertical style="width: 100%">
              <n-space v-if="editFormData.tagNames && editFormData.tagNames.length > 0">
                <n-tag
                  v-for="tag in editFormData.tagNames"
                  :key="tag"
                  closable
                  @close="handleRemoveEditTag(tag)"
                >
                  {{ tag }}
                </n-tag>
              </n-space>
              <n-input-group>
                <n-input
                  v-model:value="editTagInput"
                  :placeholder="$t('albums.enterTag')"
                  @keyup.enter="handleAddEditTag"
                />
                <n-button type="primary" @click="handleAddEditTag">
                  {{ $t('albums.addTag') }}
                </n-button>
              </n-input-group>
            </n-space>
          </n-form-item>

          <n-form-item :label="$t('albums.selectImages')">
            <n-space vertical style="width: 100%">
              <n-spin :show="loadingImages">
                <div v-if="availableImages.length === 0" style="text-align: center; padding: 24px">
                  <n-text depth="3">{{ $t('albums.noImagesAvailable') }}</n-text>
                </div>
                <div v-else class="image-selection-grid">
                  <div
                    v-for="image in availableImages"
                    :key="image.id"
                    class="image-selection-item"
                    :class="{
                      'selected': selectedImages.includes(image.id),
                      'cover': coverImageId === image.id
                    }"
                  >
                    <n-checkbox
                      :checked="selectedImages.includes(image.id)"
                      @update:checked="(checked: boolean) => handleImageSelect(image.id, checked)"
                      class="image-checkbox"
                    />
                    <div class="image-wrapper" @click="handleImageSelect(image.id, !selectedImages.includes(image.id))">
                      <n-image
                        :src="image.thumbnailUrl || image.url"
                        :alt="image.title"
                        object-fit="cover"
                        class="selection-image"
                        lazy
                      />
                    </div>
                    <div class="image-info">
                      <n-ellipsis style="font-size: 12px">{{ image.title }}</n-ellipsis>
                      <n-button
                        v-if="selectedImages.includes(image.id)"
                        size="tiny"
                        :type="coverImageId === image.id ? 'primary' : 'default'"
                        @click="handleSetCover(image.id)"
                        style="margin-top: 4px"
                      >
                        {{ coverImageId === image.id ? $t('albums.coverImage') : $t('albums.setAsCover') }}
                      </n-button>
                    </div>
                  </div>
                </div>
              </n-spin>
              <n-space justify="space-between" align="center" style="margin-top: 8px">
                <n-text depth="3">
                  {{ $t('albums.selectedImagesCount', { count: selectedImages.length }) }}
                </n-text>
                <n-pagination
                  v-if="imageTotalPages > 1"
                  v-model:page="imageCurrentPage"
                  :page-count="imageTotalPages"
                  size="small"
                  @update:page="handleImagePageChange"
                />
              </n-space>
            </n-space>
          </n-form-item>
        </n-form>
      </n-spin>

      <template #footer>
        <n-space justify="end">
          <n-button @click="handleCancelEdit">{{ $t('albums.cancel') }}</n-button>
          <n-button type="primary" :loading="updating" @click="handleUpdate">
            {{ $t('albums.save') }}
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMessage, NIcon, type FormInst, type FormRules } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import {
  SearchOutline,
  AddOutline,
  StarOutline,
  Star,
  TrashOutline,
  AlbumsOutline,
  CreateOutline
} from '@vicons/ionicons5'
import { useAlbumStore } from '@/stores/album'
import { useCategoryStore } from '@/stores/category'
import { albumApi } from '@/api/album'
import { imageApi } from '@/api/image'
import type { Album, CreateAlbumRequest, UpdateAlbumRequest } from '@/types/album'
import type { Category } from '@/types/category'
import type { ImageInfo } from '@/types/image'

const { t: $t } = useI18n()
const router = useRouter()
const route = useRoute()
const message = useMessage()
const albumStore = useAlbumStore()
const categoryStore = useCategoryStore()

const searchKeyword = ref('')
const showCreateModal = ref(false)
const creating = ref(false)
const formRef = ref<FormInst | null>(null)
const categoryId = ref<number | undefined>(undefined)
const tagInput = ref('')

const formData = reactive<CreateAlbumRequest>({
  albumName: '',
  description: '',
  categoryId: 0,
  tagNames: []
})

// Edit modal state
const showEditModal = ref(false)
const updating = ref(false)
const loadingEditData = ref(false)
const loadingImages = ref(false)
const editFormRef = ref<FormInst | null>(null)
const editTagInput = ref('')
const editingAlbumId = ref<number>(0)
const selectedImages = ref<number[]>([])
const coverImageId = ref<number | null>(null)
const availableImages = ref<ImageInfo[]>([])
const imageCurrentPage = ref(1)
const imagePageSize = ref(20)
const imageTotalPages = ref(1)
const imageTotalCount = ref(0)

const editFormData = reactive<UpdateAlbumRequest>({
  id: 0,
  albumName: '',
  description: '',
  categoryId: 0,
  tagNames: [],
  imageIds: [],
  coverImageId: undefined
})

const rules = computed((): FormRules => ({
  albumName: [
    {
      required: true,
      message: $t('albums.albumNameRequired'),
      trigger: 'blur'
    }
  ],
  categoryId: [
    {
      required: true,
      type: 'number',
      message: $t('albums.categoryRequired'),
      trigger: ['blur', 'change'],
      validator: (rule: any, value: any) => {
        return value > 0
      }
    }
  ]
}))

const editRules = computed((): FormRules => ({
  albumName: [
    {
      required: true,
      message: $t('albums.albumNameRequired'),
      trigger: 'blur'
    }
  ],
  categoryId: [
    {
      required: true,
      type: 'number',
      message: $t('albums.categoryRequired'),
      trigger: ['blur', 'change'],
      validator: (rule: any, value: any) => {
        return value > 0
      }
    }
  ]
}))

const responsiveCols = computed(() => {
  return 5
})

onMounted(() => {
  // Get categoryId from route query
  if (route.query.categoryId) {
    categoryId.value = Number(route.query.categoryId)
  }
  albumStore.fetchAlbums({ categoryId: categoryId.value })
  categoryStore.fetchCategoryTree()
})

// Convert category tree to flat list for select options
const categoryOptions = computed(() => {
  const flattenCategories = (categories: Category[], level = 0): Array<{label: string, value: number}> => {
    const result: Array<{label: string, value: number}> = []
    for (const category of categories) {
      result.push({
        label: '　'.repeat(level) + category.categoryName,
        value: category.id
      })
      if (category.children && category.children.length > 0) {
        result.push(...flattenCategories(category.children, level + 1))
      }
    }
    return result
  }
  return flattenCategories(categoryStore.categories)
})

const handleAddTag = () => {
  if (tagInput.value && !formData.tagNames?.includes(tagInput.value)) {
    if (!formData.tagNames) {
      formData.tagNames = []
    }
    formData.tagNames.push(tagInput.value)
    tagInput.value = ''
  }
}

const handleRemoveTag = (tag: string) => {
  if (formData.tagNames) {
    formData.tagNames = formData.tagNames.filter(t => t !== tag)
  }
}

const handleAddEditTag = () => {
  if (editTagInput.value && !editFormData.tagNames?.includes(editTagInput.value)) {
    if (!editFormData.tagNames) {
      editFormData.tagNames = []
    }
    editFormData.tagNames.push(editTagInput.value)
    editTagInput.value = ''
  }
}

const handleRemoveEditTag = (tag: string) => {
  if (editFormData.tagNames) {
    editFormData.tagNames = editFormData.tagNames.filter(t => t !== tag)
  }
}

// Watch for route changes to update categoryId
watch(() => route.query.categoryId, (newCategoryId) => {
  categoryId.value = newCategoryId ? Number(newCategoryId) : undefined
  albumStore.setPage(1)
  albumStore.fetchAlbums({ categoryId: categoryId.value, keyword: searchKeyword.value })
})

const handleSearch = () => {
  albumStore.fetchAlbums({ categoryId: categoryId.value, keyword: searchKeyword.value })
}

const handlePageChange = (page: number) => {
  albumStore.setPage(page)
  albumStore.fetchAlbums({ categoryId: categoryId.value, keyword: searchKeyword.value })
}

const handlePageSizeChange = (pageSize: number) => {
  albumStore.setPageSize(pageSize)
  albumStore.setPage(1)
  albumStore.fetchAlbums({ categoryId: categoryId.value, keyword: searchKeyword.value })
}

const handleCreate = async () => {
  try {
    await formRef.value?.validate()
    creating.value = true

    await albumStore.createAlbum(formData)
    message.success($t('albums.createSuccess'))
    showCreateModal.value = false
    formData.albumName = ''
    formData.description = ''
    formData.categoryId = 0
    formData.tagNames = []
    tagInput.value = ''
  } catch (error: any) {
    if (error?.message) {
      message.error(error.message)
    }
  } finally {
    creating.value = false
  }
}

const handleEditClick = async (albumId: number) => {
  console.log('[Albums] Edit button clicked for album:', albumId)
  editingAlbumId.value = albumId
  showEditModal.value = true

  // Reset state
  selectedImages.value = []
  coverImageId.value = null
  imageCurrentPage.value = 1

  // Load album details and images
  await Promise.all([
    loadAlbumDetails(albumId),
    loadAvailableImages()
  ])
}

const loadAlbumDetails = async (albumId: number) => {
  try {
    loadingEditData.value = true
    console.log('[Albums] Loading album details for album:', albumId)

    const response = await albumApi.getAlbumDetail(albumId)
    console.log('[Albums] Album details response:', response)

    const albumDetail = response.data
    console.log('[Albums] Album details data:', albumDetail)

    // Populate edit form
    editFormData.id = albumDetail.id
    editFormData.albumName = albumDetail.albumName
    editFormData.description = albumDetail.description || ''
    editFormData.categoryId = albumDetail.categoryId || 0
    editFormData.tagNames = albumDetail.tags?.map(tag => tag.tagName) || []

    // Set selected images and cover image
    if (albumDetail.images && albumDetail.images.length > 0) {
      selectedImages.value = albumDetail.images.map(img => img.id)
      console.log('[Albums] Selected images:', selectedImages.value)
    }

    // Set cover image if exists
    if (albumDetail.coverImageId) {
      coverImageId.value = albumDetail.coverImageId
      console.log('[Albums] Cover image ID:', coverImageId.value)
    }
  } catch (error: any) {
    console.error('[Albums] Error loading album details:', error)
    message.error(error.message || $t('albums.loadDetailsFailed'))
  } finally {
    loadingEditData.value = false
  }
}

const loadAvailableImages = async () => {
  try {
    loadingImages.value = true
    console.log('[Albums] Loading available images, page:', imageCurrentPage.value)

    const response = await imageApi.getImagePage({
      current: imageCurrentPage.value,
      size: imagePageSize.value
    })

    availableImages.value = response.data.records || []
    imageTotalCount.value = response.data.total || 0
    imageTotalPages.value = Math.ceil((response.data.total || 0) / imagePageSize.value)

    console.log('[Albums] Loaded', availableImages.value.length, 'images, total:', imageTotalCount.value)
  } catch (error: any) {
    console.error('[Albums] Error loading images:', error)
    message.error(error.message || $t('albums.loadImagesFailed'))
  } finally {
    loadingImages.value = false
  }
}

const handleImagePageChange = (page: number) => {
  imageCurrentPage.value = page
  loadAvailableImages()
}

const handleImageSelect = (imageId: number, checked: boolean) => {
  if (checked) {
    if (!selectedImages.value.includes(imageId)) {
      selectedImages.value.push(imageId)
      console.log('[Albums] Image selected:', imageId, 'Total selected:', selectedImages.value.length)

      // If this is the first image, set it as cover
      if (selectedImages.value.length === 1) {
        coverImageId.value = imageId
        console.log('[Albums] Auto-set first image as cover:', imageId)
      }
    }
  } else {
    selectedImages.value = selectedImages.value.filter(id => id !== imageId)
    console.log('[Albums] Image deselected:', imageId, 'Total selected:', selectedImages.value.length)

    // If the cover image was deselected, set a new cover or clear it
    if (coverImageId.value === imageId) {
      coverImageId.value = selectedImages.value.length > 0 ? selectedImages.value[0] : null
      console.log('[Albums] Cover image changed to:', coverImageId.value)
    }
  }
}

const handleSetCover = (imageId: number) => {
  if (selectedImages.value.includes(imageId)) {
    coverImageId.value = imageId
    console.log('[Albums] Cover image set to:', imageId)
  }
}

const handleUpdate = async () => {
  try {
    await editFormRef.value?.validate()
    updating.value = true

    console.log('[Albums] Updating album:', editingAlbumId.value)
    console.log('[Albums] Selected images:', selectedImages.value)
    console.log('[Albums] Cover image:', coverImageId.value)

    // Prepare update data
    const updateData: UpdateAlbumRequest = {
      id: editFormData.id,
      albumName: editFormData.albumName,
      description: editFormData.description,
      categoryId: editFormData.categoryId,
      tagNames: editFormData.tagNames,
      imageIds: selectedImages.value,
      coverImageId: coverImageId.value || undefined
    }

    await albumApi.updateAlbum(updateData)
    console.log('[Albums] Album updated successfully')

    message.success($t('albums.updateSuccess'))
    showEditModal.value = false

    // Refresh album list
    await albumStore.fetchAlbums({ categoryId: categoryId.value, keyword: searchKeyword.value })
  } catch (error: any) {
    console.error('[Albums] Error updating album:', error)
    if (error?.message) {
      message.error(error.message)
    } else {
      message.error($t('albums.updateFailed'))
    }
  } finally {
    updating.value = false
  }
}

const handleCancelEdit = () => {
  showEditModal.value = false
  // Reset form data
  editFormData.id = 0
  editFormData.albumName = ''
  editFormData.description = ''
  editFormData.categoryId = 0
  editFormData.tagNames = []
  editTagInput.value = ''
  selectedImages.value = []
  coverImageId.value = null
  availableImages.value = []
}

const handleToggleStar = async (album: Album) => {
  try {
    if (album.isStared) {
      await albumStore.unstarAlbum(album.id)
      message.success($t('albums.removedFromFavorites'))
    } else {
      await albumStore.starAlbum(album.id)
      message.success($t('albums.addedToFavorites'))
    }
  } catch (error: any) {
    message.error(error.message || $t('albums.operationFailed'))
  }
}

const handleDelete = async (albumId: number) => {
  try {
    await albumStore.deleteAlbum(albumId)
    message.success($t('albums.deleteSuccess'))
  } catch (error: any) {
    message.error(error.message || $t('albums.deleteFailed'))
  }
}

const handleAlbumClick = (albumId: number) => {
  router.push(`/albums/${albumId}`)
}
</script>

<style scoped>
.albums-page {
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 16px;
}

.album-card {
  height: 100%;
  cursor: pointer;
}

.album-cover {
  aspect-ratio: 16 / 9;
  overflow: hidden;
  border-radius: 4px;
  background: var(--n-color-embedded);
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.album-card:hover .cover-image {
  transform: scale(1.05);
}

.empty-cover {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--n-text-color-disabled);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 32px;
  padding: 16px 0;
}

.pagination-info {
  margin-right: 16px;
  font-size: 14px;
  color: var(--n-text-color);
}

/* Edit modal image selection styles */
.image-selection-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-top: 8px;
}

.image-selection-item {
  position: relative;
  border: 2px solid transparent;
  border-radius: 8px;
  padding: 8px;
  transition: all 0.3s ease;
  background: var(--n-color-embedded);
}

.image-selection-item:hover {
  border-color: var(--n-border-color);
  background: var(--n-color-embedded-popover);
}

.image-selection-item.selected {
  border-color: var(--n-color-target);
  background: var(--n-color-target-alpha);
}

.image-selection-item.cover {
  border-color: var(--n-color-primary);
  background: var(--n-color-primary-alpha);
}

.image-checkbox {
  position: absolute;
  top: 12px;
  left: 12px;
  z-index: 10;
}

.image-wrapper {
  aspect-ratio: 4 / 3;
  overflow: hidden;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 8px;
}

.selection-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style>
