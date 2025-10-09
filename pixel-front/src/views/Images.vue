<template>
  <div class="images-page">
    <n-space vertical :size="24">
      <n-space justify="space-between" align="center">
        <h1>My Images</h1>
        <n-input
          v-model:value="searchKeyword"
          placeholder="Search images..."
          clearable
          style="width: 300px"
          @update:value="handleSearch"
        >
          <template #prefix>
            <n-icon><SearchOutline /></n-icon>
          </template>
        </n-input>
      </n-space>

      <n-spin :show="imageStore.loading">
        <n-grid x-gap="16" y-gap="16" :cols="responsiveCols">
          <n-grid-item v-for="image in imageStore.images" :key="image.id">
            <n-card
              class="image-card"
              :segmented="{ content: 'soft' }"
              hoverable
            >
              <div class="image-wrapper" @click="handleImageClick(image)">
                <n-image
                  :src="image.fileUrl"
                  :alt="image.title || image.fileName"
                  object-fit="cover"
                  class="image"
                  lazy
                />
              </div>

              <template #footer>
                <n-space justify="space-between" align="center">
                  <n-ellipsis style="max-width: 200px">
                    {{ image.title || image.fileName }}
                  </n-ellipsis>

                  <n-space>
                    <n-button
                      text
                      @click="handleToggleStar(image)"
                    >
                      <template #icon>
                        <n-icon :color="image.isStared ? '#f59e0b' : undefined">
                          <Star v-if="image.isStared" />
                          <StarOutline v-else />
                        </n-icon>
                      </template>
                      {{ image.starCount }}
                    </n-button>

                    <n-popconfirm
                      @positive-click="handleDelete(image.id)"
                    >
                      <template #trigger>
                        <n-button text type="error">
                          <template #icon>
                            <n-icon><TrashOutline /></n-icon>
                          </template>
                        </n-button>
                      </template>
                      Are you sure you want to delete this image?
                    </n-popconfirm>
                  </n-space>
                </n-space>
              </template>
            </n-card>
          </n-grid-item>
        </n-grid>

        <n-empty
          v-if="!imageStore.loading && imageStore.images.length === 0"
          description="No images found"
          style="margin-top: 60px"
        >
          <template #extra>
            <n-button @click="$router.push('/')">
              Upload your first image
            </n-button>
          </template>
        </n-empty>

        <n-pagination
          v-if="imageStore.total > imageStore.pageSize"
          v-model:page="imageStore.currentPage"
          v-model:page-size="imageStore.pageSize"
          :page-count="Math.ceil(imageStore.total / imageStore.pageSize)"
          show-size-picker
          :page-sizes="[20, 40, 60, 100]"
          style="margin-top: 24px; justify-content: center"
          @update:page="handlePageChange"
          @update:page-size="handlePageSizeChange"
        />
      </n-spin>
    </n-space>

    <!-- Image Detail Modal -->
    <n-modal
      v-model:show="showDetailModal"
      preset="card"
      :title="currentImage?.title || currentImage?.fileName"
      style="width: 90%; max-width: 1200px"
    >
      <div v-if="currentImage" class="image-detail">
        <n-image
          :src="currentImage.fileUrl"
          :alt="currentImage.title || currentImage.fileName"
          class="detail-image"
        />

        <n-descriptions bordered :column="2" style="margin-top: 16px">
          <n-descriptions-item label="File Name">
            {{ currentImage.fileName }}
          </n-descriptions-item>
          <n-descriptions-item label="Upload Time">
            {{ currentImage.uploadTime }}
          </n-descriptions-item>
          <n-descriptions-item label="File Size">
            {{ formatFileSize(currentImage.fileSize) }}
          </n-descriptions-item>
          <n-descriptions-item label="Dimensions">
            {{ currentImage.width }} × {{ currentImage.height }}
          </n-descriptions-item>
        </n-descriptions>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useMessage, NIcon } from 'naive-ui'
import { SearchOutline, StarOutline, Star, TrashOutline } from '@vicons/ionicons5'
import { useImageStore } from '@/stores/image'
import type { ImageInfo } from '@/types/image'

const message = useMessage()
const imageStore = useImageStore()

const searchKeyword = ref('')
const showDetailModal = ref(false)
const currentImage = ref<ImageInfo | null>(null)

const responsiveCols = computed(() => {
  return 'xs:1 s:2 m:3 l:4 xl:5 2xl:6'
})

onMounted(() => {
  imageStore.fetchImages()
})

const handleSearch = () => {
  imageStore.fetchImages({ keyword: searchKeyword.value })
}

const handlePageChange = (page: number) => {
  imageStore.setPage(page)
  imageStore.fetchImages({ keyword: searchKeyword.value })
}

const handlePageSizeChange = (pageSize: number) => {
  imageStore.setPageSize(pageSize)
  imageStore.setPage(1)
  imageStore.fetchImages({ keyword: searchKeyword.value })
}

const handleToggleStar = async (image: ImageInfo) => {
  try {
    if (image.isStared) {
      await imageStore.unstarImage(image.id)
      message.success('Removed from favorites')
    } else {
      await imageStore.starImage(image.id)
      message.success('Added to favorites')
    }
  } catch (error: any) {
    message.error(error.message || 'Operation failed')
  }
}

const handleDelete = async (imageId: number) => {
  try {
    await imageStore.deleteImage(imageId)
    message.success('Image deleted successfully')
  } catch (error: any) {
    message.error(error.message || 'Failed to delete image')
  }
}

const handleImageClick = (image: ImageInfo) => {
  currentImage.value = image
  showDetailModal.value = true
}

const formatFileSize = (bytes: number) => {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(2) + ' MB'
}
</script>

<style scoped>
.images-page {
  max-width: 1600px;
  margin: 0 auto;
}

.image-card {
  height: 100%;
}

.image-wrapper {
  cursor: pointer;
  overflow: hidden;
  border-radius: 4px;
  aspect-ratio: 1;
}

.image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.image-wrapper:hover .image {
  transform: scale(1.05);
}

.detail-image {
  max-width: 100%;
  max-height: 70vh;
  object-fit: contain;
}
</style>
