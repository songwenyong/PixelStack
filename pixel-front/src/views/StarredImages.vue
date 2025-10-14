<template>
  <div class="starred-images-page">
    <n-space vertical :size="24">
      <n-space justify="space-between" align="center">
        <h1>{{ $t('nav.starredImages') }}</h1>
      </n-space>

      <n-spin :show="loading">
        <n-grid x-gap="16" y-gap="16" :cols="5">
          <n-grid-item v-for="image in images" :key="image.id">
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
                  </n-space>
                </n-space>
              </template>
            </n-card>
          </n-grid-item>
        </n-grid>

        <n-empty
          v-if="!loading && images.length === 0"
          :description="$t('images.noImagesFound')"
          style="margin-top: 60px"
        >
          <template #extra>
            <n-button type="primary" @click="$router.push('/images')">
              {{ $t('nav.allImages') }}
            </n-button>
          </template>
        </n-empty>

        <div v-if="total > 0" class="pagination-wrapper">
          <n-pagination
            v-model:page="currentPage"
            v-model:page-size="pageSize"
            :page-count="Math.ceil(total / pageSize)"
            :item-count="total"
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
          <n-descriptions-item :label="$t('image.format')">
            {{ currentImage.format }}
          </n-descriptions-item>
          <n-descriptions-item :label="$t('image.creator')">
            {{ currentImage.creatorName || currentImage.creator }}
          </n-descriptions-item>
        </n-descriptions>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useMessage, NIcon } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { StarOutline, Star } from '@vicons/ionicons5'
import { imageApi } from '@/api/image'
import type { ImageInfo } from '@/types/image'

const { t: $t } = useI18n()
const message = useMessage()

const images = ref<ImageInfo[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(50)
const total = ref(0)
const showDetailModal = ref(false)
const currentImage = ref<ImageInfo | null>(null)

const fetchStarredImages = async () => {
  try {
    loading.value = true
    const response = await imageApi.getStaredImagePage({
      current: currentPage.value,
      size: pageSize.value
    })
    images.value = response.data.records || []
    total.value = response.data.total || 0
  } catch (error: any) {
    message.error(error.message || $t('images.operationFailed'))
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStarredImages()
})

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchStarredImages()
}

const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchStarredImages()
}

const handleToggleStar = async (image: ImageInfo) => {
  try {
    if (image.isStared) {
      await imageApi.unstarImage(image.id)
      message.success($t('images.removedFromFavorites'))
      // Remove from list
      images.value = images.value.filter(img => img.id !== image.id)
      total.value -= 1
    } else {
      await imageApi.starImage(image.id)
      message.success($t('images.addedToFavorites'))
      fetchStarredImages()
    }
  } catch (error: any) {
    message.error(error.message || $t('images.operationFailed'))
  }
}

const handleImageClick = (image: ImageInfo) => {
  currentImage.value = image
  showDetailModal.value = true
}
</script>

<style scoped>
.starred-images-page {
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 16px;
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
</style>
