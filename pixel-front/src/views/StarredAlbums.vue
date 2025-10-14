<template>
  <div class="starred-albums-page">
    <n-space vertical :size="24">
      <n-space justify="space-between" align="center">
        <h1>{{ $t('nav.starredAlbums') }}</h1>
      </n-space>

      <n-spin :show="loading">
        <n-grid x-gap="16" y-gap="16" :cols="5">
          <n-grid-item v-for="album in albums" :key="album.id">
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
                    </n-space>
                  </n-space>
                </n-space>
              </template>
            </n-card>
          </n-grid-item>
        </n-grid>

        <n-empty
          v-if="!loading && albums.length === 0"
          :description="$t('albums.noAlbumsFound')"
          style="margin-top: 60px"
        >
          <template #extra>
            <n-button type="primary" @click="$router.push('/albums')">
              {{ $t('nav.allAlbums') }}
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, NIcon } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import {
  StarOutline,
  Star,
  AlbumsOutline
} from '@vicons/ionicons5'
import { albumApi } from '@/api/album'
import type { Album } from '@/types/album'

const { t: $t } = useI18n()
const router = useRouter()
const message = useMessage()

const albums = ref<Album[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(50)
const total = ref(0)

const fetchStarredAlbums = async () => {
  try {
    loading.value = true
    const response = await albumApi.getStaredAlbumPage({
      current: currentPage.value,
      size: pageSize.value
    })
    albums.value = response.data.records || []
    total.value = response.data.total || 0
  } catch (error: any) {
    message.error(error.message || $t('albums.operationFailed'))
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStarredAlbums()
})

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchStarredAlbums()
}

const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchStarredAlbums()
}

const handleToggleStar = async (album: Album) => {
  try {
    if (album.isStared) {
      await albumApi.unstarAlbum(album.id)
      message.success($t('albums.removedFromFavorites'))
      // Remove from list
      albums.value = albums.value.filter(a => a.id !== album.id)
      total.value -= 1
    } else {
      await albumApi.starAlbum(album.id)
      message.success($t('albums.addedToFavorites'))
      fetchStarredAlbums()
    }
  } catch (error: any) {
    message.error(error.message || $t('albums.operationFailed'))
  }
}

const handleAlbumClick = (albumId: number) => {
  router.push(`/albums/${albumId}`)
}
</script>

<style scoped>
.starred-albums-page {
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
</style>
