<template>
  <div class="album-detail-page">
    <n-spin :show="loading">
      <n-space vertical :size="24">
        <!-- Album Header -->
        <n-card v-if="albumDetail">
          <n-space vertical :size="16">
            <n-space justify="space-between" align="center">
              <n-space align="center">
                <n-button text @click="handleBack">
                  <template #icon>
                    <n-icon><ArrowBackOutline /></n-icon>
                  </template>
                </n-button>
                <h1 style="margin: 0">{{ albumDetail.albumName }}</h1>
              </n-space>

              <n-space>
                <n-button @click="handleToggleStar">
                  <template #icon>
                    <n-icon :color="albumDetail.isStared ? '#f59e0b' : undefined">
                      <Star v-if="albumDetail.isStared" />
                      <StarOutline v-else />
                    </n-icon>
                  </template>
                  {{ albumDetail.isStared ? $t('albums.unfavorite') : $t('albums.favorite') }}
                </n-button>
              </n-space>
            </n-space>

            <n-descriptions bordered :column="3">
              <n-descriptions-item :label="$t('albums.category')">
                {{ albumDetail.categoryName || '-' }}
              </n-descriptions-item>
              <n-descriptions-item :label="$t('albums.creator')">
                {{ albumDetail.creatorName }}
              </n-descriptions-item>
              <n-descriptions-item :label="$t('albums.createTime')">
                {{ albumDetail.createdAt }}
              </n-descriptions-item>
              <n-descriptions-item :label="$t('albums.imageCount')" :span="3">
                {{ albumDetail.imageCount }} {{ $t('albums.imagesCount') }}
              </n-descriptions-item>
              <n-descriptions-item v-if="albumDetail.description" :label="$t('albums.description')" :span="3">
                {{ albumDetail.description }}
              </n-descriptions-item>
              <n-descriptions-item v-if="albumDetail.tagNames && albumDetail.tagNames.length > 0" :label="$t('albums.tags')" :span="3">
                <n-space>
                  <n-tag v-for="tag in albumDetail.tagNames.split(',')" :key="tag" type="info">
                    {{ tag }}
                  </n-tag>
                </n-space>
              </n-descriptions-item>
            </n-descriptions>
          </n-space>
        </n-card>

        <!-- Images Grid -->
        <n-card :title="$t('albums.images')">
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
                        @click="handleToggleImageStar(image)"
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
          />

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
        </n-card>
      </n-space>
    </n-spin>

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
import { useRouter, useRoute } from 'vue-router'
import { useMessage, NIcon } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import {
  StarOutline,
  Star,
  ArrowBackOutline
} from '@vicons/ionicons5'
import { albumApi } from '@/api/album'
import { imageApi } from '@/api/image'
import type { Album } from '@/types/album'
import type { ImageInfo } from '@/types/image'

const { t: $t } = useI18n()
const router = useRouter()
const route = useRoute()
const message = useMessage()

const albumDetail = ref<Album | null>(null)
const images = ref<ImageInfo[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(50)
const total = ref(0)
const showDetailModal = ref(false)
const currentImage = ref<ImageInfo | null>(null)

const albumId = Number(route.params.id)

const fetchAlbumDetail = async () => {
  try {
    const response = await albumApi.getAlbumDetail(albumId)
    albumDetail.value = response.data
  } catch (error: any) {
    message.error(error.message || $t('albums.operationFailed'))
  }
}

const fetchAlbumImages = async () => {
  try {
    loading.value = true
    const response = await albumApi.getAlbumImages(albumId, {
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
  fetchAlbumDetail()
  fetchAlbumImages()
})

const handleBack = () => {
  router.back()
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchAlbumImages()
}

const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchAlbumImages()
}

const handleToggleStar = async () => {
  if (!albumDetail.value) return

  try {
    if (albumDetail.value.isStared) {
      await albumApi.unstarAlbum(albumDetail.value.id)
      message.success($t('albums.removedFromFavorites'))
      albumDetail.value.isStared = false
    } else {
      await albumApi.starAlbum(albumDetail.value.id)
      message.success($t('albums.addedToFavorites'))
      albumDetail.value.isStared = true
    }
  } catch (error: any) {
    message.error(error.message || $t('albums.operationFailed'))
  }
}

const handleToggleImageStar = async (image: ImageInfo) => {
  try {
    if (image.isStared) {
      await imageApi.unstarImage(image.id)
      message.success($t('images.removedFromFavorites'))
      image.isStared = false
    } else {
      await imageApi.starImage(image.id)
      message.success($t('images.addedToFavorites'))
      image.isStared = true
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
.album-detail-page {
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
