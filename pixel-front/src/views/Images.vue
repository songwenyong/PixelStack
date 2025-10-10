<template>
  <div class="images-page">
    <n-space vertical :size="24">
      <n-space justify="space-between" align="center">
        <h1>{{ $t('images.myImages') }}</h1>
        <n-input
          v-model:value="searchKeyword"
          :placeholder="$t('images.searchImages')"
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
                  :src="image.url"
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
                      {{ $t('images.deleteConfirmText') }}
                    </n-popconfirm>
                  </n-space>
                </n-space>
              </template>
            </n-card>
          </n-grid-item>
        </n-grid>

        <n-empty
          v-if="!imageStore.loading && imageStore.images.length === 0"
          :description="$t('images.noImagesFound')"
          style="margin-top: 60px"
        >
          <template #extra>
            <n-button @click="$router.push('/')">
              {{ $t('images.uploadFirstImage') }}
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
          :src="currentImage.url"
          :alt="currentImage.title || currentImage.fileName"
          class="detail-image"
        />

        <n-descriptions bordered :column="2" style="margin-top: 16px">
          <n-descriptions-item :label="$t('images.fileName')">
            {{ currentImage.fileName }}
          </n-descriptions-item>
          <n-descriptions-item :label="$t('images.uploadTime')">
            {{ currentImage.createdAt }}
          </n-descriptions-item>
          <n-descriptions-item :label="$t('images.format')">
            {{ currentImage.format }}
          </n-descriptions-item>
          <n-descriptions-item :label="$t('images.creator')">
            {{ currentImage.creatorName || currentImage.creator }}
          </n-descriptions-item>
        </n-descriptions>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useMessage, NIcon } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { SearchOutline, StarOutline, Star, TrashOutline } from '@vicons/ionicons5'
import { useImageStore } from '@/stores/image'
import type { ImageInfo } from '@/types/image'

const { t: $t } = useI18n()
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
      message.success($t('images.removedFromFavorites'))
    } else {
      await imageStore.starImage(image.id)
      message.success($t('images.addedToFavorites'))
    }
  } catch (error: any) {
    message.error(error.message || $t('images.operationFailed'))
  }
}

const handleDelete = async (imageId: number) => {
  try {
    await imageStore.deleteImage(imageId)
    message.success($t('images.imageDeleteSuccess'))
  } catch (error: any) {
    message.error(error.message || $t('images.imageDeleteFailed'))
  }
}

const handleImageClick = (image: ImageInfo) => {
  currentImage.value = image
  showDetailModal.value = true
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
