<template>
  <div class="home">
    <n-space vertical :size="32">
      <div class="hero">
        <n-gradient-text :size="48" type="primary">
          {{ $t('home.welcomeTitle') }}
        </n-gradient-text>
        <p class="subtitle">
          {{ $t('home.subtitle') }}
        </p>
      </div>

      <n-grid :cols="3" :x-gap="24" responsive="screen">
        <n-grid-item>
          <n-card hoverable>
            <template #header>
              <n-space align="center">
                <n-icon size="28" color="#3b82f6"><ImagesOutline /></n-icon>
                <span>{{ $t('home.myImages') }}</span>
              </n-space>
            </template>
            <n-statistic :label="$t('home.totalImages')" :value="stats.totalImages" />
            <template #footer>
              <n-button text type="primary" @click="$router.push('/images')">
                {{ $t('home.viewAll') }}
              </n-button>
            </template>
          </n-card>
        </n-grid-item>

        <n-grid-item>
          <n-card hoverable>
            <template #header>
              <n-space align="center">
                <n-icon size="28" color="#10b981"><AlbumsOutline /></n-icon>
                <span>{{ $t('home.myAlbums') }}</span>
              </n-space>
            </template>
            <n-statistic :label="$t('home.totalAlbums')" :value="stats.totalAlbums" />
            <template #footer>
              <n-button text type="primary" @click="$router.push('/albums')">
                View All →
              </n-button>
            </template>
          </n-card>
        </n-grid-item>

        <n-grid-item>
          <n-card hoverable>
            <template #header>
              <n-space align="center">
                <n-icon size="28" color="#f59e0b"><StarOutline /></n-icon>
                <span>{{ $t('home.favorites') }}</span>
              </n-space>
            </template>
            <n-statistic :label="$t('home.starredItems')" :value="stats.totalFavorites" />
            <template #footer>
              <n-button text type="primary" @click="$router.push('/favorites/images')">
                View All →
              </n-button>
            </template>
          </n-card>
        </n-grid-item>
      </n-grid>

      <n-card :title="$t('home.quickActions')">
        <n-space>
          <n-button type="primary" @click="showUploadDrawer = true">
            <template #icon>
              <n-icon><CloudUploadOutline /></n-icon>
            </template>
            {{ $t('home.uploadImage') }}
          </n-button>

          <n-button @click="$router.push('/albums')">
            <template #icon>
              <n-icon><AddOutline /></n-icon>
            </template>
            {{ $t('home.createAlbum') }}
          </n-button>

          <n-button @click="$router.push('/categories')">
            <template #icon>
              <n-icon><FolderOutline /></n-icon>
            </template>
            {{ $t('home.manageCategories') }}
          </n-button>
        </n-space>
      </n-card>

      <n-card :title="$t('home.recentImages')">
        <n-spin :show="loading">
          <n-grid :cols="5" :x-gap="16" :y-gap="16" responsive="screen">
            <n-grid-item v-for="image in recentImages" :key="image.id">
              <n-card
                hoverable
                @click="$router.push('/images')"
                style="cursor: pointer"
              >
                <div class="recent-image">
                  <n-image
                    :src="image.url"
                    :alt="image.title || image.fileName"
                    object-fit="cover"
                    lazy
                  />
                </div>
              </n-card>
            </n-grid-item>
          </n-grid>

          <n-empty
            v-if="!loading && recentImages.length === 0"
            :description="$t('home.noImagesYet')"
            style="margin-top: 40px"
          >
            <template #extra>
              <n-button type="primary" @click="showUploadDrawer = true">
                {{ $t('home.uploadFirstImage') }}
              </n-button>
            </template>
          </n-empty>
        </n-spin>
      </n-card>
    </n-space>

    <!-- Upload Drawer -->
    <n-drawer v-model:show="showUploadDrawer" width="400">
      <n-drawer-content :title="$t('home.uploadImage')">
        <n-upload
          :custom-request="handleUpload"
          :max="1"
          accept="image/*"
          list-type="image-card"
        >
          <n-button>{{ $t('home.selectImage') }}</n-button>
        </n-upload>

        <n-form-item :label="$t('image.imageTitle') + ' (' + $t('common.optional') + ')'" style="margin-top: 16px">
          <n-input
            v-model:value="uploadTitle"
            :placeholder="$t('image.enterImageTitle')"
          />
        </n-form-item>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, NIcon, type UploadCustomRequestOptions } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import {
  ImagesOutline,
  AlbumsOutline,
  StarOutline,
  CloudUploadOutline,
  AddOutline,
  FolderOutline
} from '@vicons/ionicons5'
import { useImageStore } from '@/stores/image'
import { useAlbumStore } from '@/stores/album'
import type { ImageInfo } from '@/types/image'

const { t: $t } = useI18n()
const router = useRouter()
const message = useMessage()
const imageStore = useImageStore()
const albumStore = useAlbumStore()

const loading = ref(false)
const showUploadDrawer = ref(false)
const uploadTitle = ref('')
const recentImages = ref<ImageInfo[]>([])

const stats = reactive({
  totalImages: 0,
  totalAlbums: 0,
  totalFavorites: 0
})

onMounted(async () => {
  loading.value = true
  try {
    // Fetch recent images
    const imagesRes = await imageStore.fetchImages({ current: 1, size: 10 })
    recentImages.value = imagesRes.data.records.slice(0, 10)
    stats.totalImages = imagesRes.data.total

    // Fetch albums count
    const albumsRes = await albumStore.fetchAlbums({ current: 1, size: 1 })
    stats.totalAlbums = albumsRes.data.total

    // Fetch favorites count
    const favoritesRes = await imageStore.fetchStaredImages({ current: 1, size: 1 })
    stats.totalFavorites = favoritesRes.data.total
  } catch (error: any) {
    console.error('Failed to load home data:', error)
  } finally {
    loading.value = false
  }
})

const handleUpload = async (options: UploadCustomRequestOptions) => {
  try {
    const file = options.file.file as File
    await imageStore.uploadImage(file, uploadTitle.value)
    message.success($t('home.uploadSuccess'))
    showUploadDrawer.value = false
    uploadTitle.value = ''
    options.onFinish()

    // Refresh recent images
    const res = await imageStore.fetchImages({ current: 1, size: 10 })
    recentImages.value = res.data.records.slice(0, 10)
    stats.totalImages = res.data.total
  } catch (error: any) {
    message.error(error.message || $t('home.uploadFailed'))
    options.onError()
  }
}
</script>

<style scoped>
.home {
  max-width: 1400px;
  margin: 0 auto;
}

.hero {
  text-align: center;
  padding: 40px 0;
}

.subtitle {
  font-size: 18px;
  color: var(--n-text-color-2);
  margin-top: 16px;
}

.recent-image {
  aspect-ratio: 1;
  overflow: hidden;
  border-radius: 4px;
}

.recent-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
