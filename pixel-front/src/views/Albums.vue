<template>
  <div class="albums-page">
    <n-space vertical :size="24">
      <n-space justify="space-between" align="center">
        <h1>My Albums</h1>
        <n-space>
          <n-input
            v-model:value="searchKeyword"
            placeholder="Search albums..."
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
            Create Album
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
                      {{ album.imageCount }} images
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
                        Are you sure you want to delete this album?
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
          description="No albums found"
          style="margin-top: 60px"
        >
          <template #extra>
            <n-button type="primary" @click="showCreateModal = true">
              Create your first album
            </n-button>
          </template>
        </n-empty>

        <n-pagination
          v-if="albumStore.total > albumStore.pageSize"
          v-model:page="albumStore.currentPage"
          v-model:page-size="albumStore.pageSize"
          :page-count="Math.ceil(albumStore.total / albumStore.pageSize)"
          show-size-picker
          :page-sizes="[20, 40, 60]"
          style="margin-top: 24px; justify-content: center"
          @update:page="handlePageChange"
          @update:page-size="handlePageSizeChange"
        />
      </n-spin>
    </n-space>

    <!-- Create Album Modal -->
    <n-modal
      v-model:show="showCreateModal"
      preset="card"
      title="Create Album"
      style="width: 600px"
    >
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="top"
      >
        <n-form-item label="Album Name" path="albumName">
          <n-input
            v-model:value="formData.albumName"
            placeholder="Enter album name"
          />
        </n-form-item>

        <n-form-item label="Description" path="description">
          <n-input
            v-model:value="formData.description"
            type="textarea"
            placeholder="Enter album description (optional)"
            :rows="3"
          />
        </n-form-item>
      </n-form>

      <template #footer>
        <n-space justify="end">
          <n-button @click="showCreateModal = false">Cancel</n-button>
          <n-button type="primary" :loading="creating" @click="handleCreate">
            Create
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, NIcon, type FormInst, type FormRules } from 'naive-ui'
import {
  SearchOutline,
  AddOutline,
  StarOutline,
  Star,
  TrashOutline,
  AlbumsOutline
} from '@vicons/ionicons5'
import { useAlbumStore } from '@/stores/album'
import type { Album, CreateAlbumRequest } from '@/types/album'

const router = useRouter()
const message = useMessage()
const albumStore = useAlbumStore()

const searchKeyword = ref('')
const showCreateModal = ref(false)
const creating = ref(false)
const formRef = ref<FormInst | null>(null)

const formData = reactive<CreateAlbumRequest>({
  albumName: '',
  description: ''
})

const rules: FormRules = {
  albumName: [
    {
      required: true,
      message: 'Please enter album name',
      trigger: 'blur'
    }
  ]
}

const responsiveCols = computed(() => {
  return 'xs:1 s:2 m:3 l:4 xl:4 2xl:5'
})

onMounted(() => {
  albumStore.fetchAlbums()
})

const handleSearch = () => {
  albumStore.fetchAlbums({ keyword: searchKeyword.value })
}

const handlePageChange = (page: number) => {
  albumStore.setPage(page)
  albumStore.fetchAlbums({ keyword: searchKeyword.value })
}

const handlePageSizeChange = (pageSize: number) => {
  albumStore.setPageSize(pageSize)
  albumStore.setPage(1)
  albumStore.fetchAlbums({ keyword: searchKeyword.value })
}

const handleCreate = async () => {
  try {
    await formRef.value?.validate()
    creating.value = true

    await albumStore.createAlbum(formData)
    message.success('Album created successfully')
    showCreateModal.value = false
    formData.albumName = ''
    formData.description = ''
  } catch (error: any) {
    if (error?.message) {
      message.error(error.message)
    }
  } finally {
    creating.value = false
  }
}

const handleToggleStar = async (album: Album) => {
  try {
    if (album.isStared) {
      await albumStore.unstarAlbum(album.id)
      message.success('Removed from favorites')
    } else {
      await albumStore.starAlbum(album.id)
      message.success('Added to favorites')
    }
  } catch (error: any) {
    message.error(error.message || 'Operation failed')
  }
}

const handleDelete = async (albumId: number) => {
  try {
    await albumStore.deleteAlbum(albumId)
    message.success('Album deleted successfully')
  } catch (error: any) {
    message.error(error.message || 'Failed to delete album')
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
</style>
