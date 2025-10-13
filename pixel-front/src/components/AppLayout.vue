<template>
  <n-layout has-sider class="app-layout">
    <n-layout-sider
      bordered
      collapse-mode="width"
      :collapsed-width="64"
      :width="240"
      :collapsed="collapsed"
      show-trigger
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <n-menu
        :collapsed="collapsed"
        :collapsed-width="64"
        :collapsed-icon-size="22"
        :options="menuOptions"
        :value="activeKey"
        @update:value="handleMenuSelect"
      />
    </n-layout-sider>

    <n-layout>
      <n-layout-header bordered class="header">
        <div class="header-content">
          <h2 class="logo">PixelStack</h2>
          <n-space>
            <n-button
              v-if="userStore.isLoggedIn"
              @click="showUploadModal = true"
            >
              <template #icon>
                <n-icon><CloudUploadOutline /></n-icon>
              </template>
              {{ $t('common.upload') }}
            </n-button>

            <n-dropdown
              v-if="userStore.isLoggedIn"
              :options="userMenuOptions"
              @select="handleUserMenuSelect"
            >
              <n-button circle>
                <template #icon>
                  <n-icon><PersonOutline /></n-icon>
                </template>
              </n-button>
            </n-dropdown>

            <n-dropdown
              v-if="userStore.isLoggedIn"
              :options="languageOptions"
              @select="handleLanguageSelect"
            >
              <n-button circle>
                <template #icon>
                  <n-icon><LanguageOutline /></n-icon>
                </template>
              </n-button>
            </n-dropdown>

            <n-button
              v-else
              @click="router.push('/login')"
            >
              {{ $t('auth.login') }}
            </n-button>
          </n-space>
        </div>
      </n-layout-header>

      <n-layout-content class="content">
        <router-view />
      </n-layout-content>
    </n-layout>

    <!-- Upload Modal -->
    <n-modal
      v-model:show="showUploadModal"
      preset="card"
      :title="$t('image.uploadImage')"
      style="width: 600px"
    >
      <n-upload
        :custom-request="handleUpload"
        :max="1"
        accept="image/*"
        list-type="image-card"
      >
        <n-button>{{ $t('image.selectImage') }}</n-button>
      </n-upload>

      <n-form-item :label="$t('image.imageTitle') + ' (' + $t('common.optional') + ')'" style="margin-top: 16px">
        <n-input
          v-model:value="uploadTitle"
          :placeholder="$t('image.enterImageTitle')"
        />
      </n-form-item>
    </n-modal>
  </n-layout>
</template>

<script setup lang="ts">
import { ref, computed, h, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMessage, NIcon } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import {
  HomeOutline,
  ImagesOutline,
  AlbumsOutline,
  FolderOutline,
  StarOutline,
  CloudUploadOutline,
  PersonOutline,
  LogOutOutline,
  SettingsOutline,
  LanguageOutline
} from '@vicons/ionicons5'
import { useUserStore } from '@/stores/user'
import { useImageStore } from '@/stores/image'
import { useAppStore } from '@/stores/app'
import { useCategoryStore } from '@/stores/category'
import { LOCALE_OPTIONS } from '@/i18n'
import type { UploadCustomRequestOptions } from 'naive-ui'
import type { Category } from '@/types/category'

const { t: $t } = useI18n()
const router = useRouter()
const route = useRoute()
const message = useMessage()
const userStore = useUserStore()
const imageStore = useImageStore()
const appStore = useAppStore()
const categoryStore = useCategoryStore()

const collapsed = ref(false)
const showUploadModal = ref(false)
const uploadTitle = ref('')

const activeKey = computed(() => {
  // Extract the base path and categoryId from query params
  if (route.query.categoryId) {
    return `${route.path}?categoryId=${route.query.categoryId}`
  }
  return route.path
})

onMounted(() => {
  categoryStore.fetchCategoryTree()
})

const renderIcon = (icon: any) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

// Convert category tree to menu options
const convertCategoriesToMenuOptions = (categories: Category[], type: 'images' | 'albums'): any[] => {
  return categories.map(category => ({
    label: category.categoryName,
    key: `/${type}?categoryId=${category.id}`,
    children: category.children && category.children.length > 0
      ? convertCategoriesToMenuOptions(category.children, type)
      : undefined
  }))
}

const menuOptions = computed(() => {
  const imagesChildren = categoryStore.categories.length > 0
    ? [
        { label: $t('nav.allImages'), key: '/images' },
        { type: 'divider', key: 'images-divider' },
        ...convertCategoriesToMenuOptions(categoryStore.categories, 'images')
      ]
    : undefined

  const albumsChildren = categoryStore.categories.length > 0
    ? [
        { label: $t('nav.allAlbums'), key: '/albums' },
        { type: 'divider', key: 'albums-divider' },
        ...convertCategoriesToMenuOptions(categoryStore.categories, 'albums')
      ]
    : undefined

  return [
    {
      label: $t('nav.home'),
      key: '/',
      icon: renderIcon(HomeOutline)
    },
    {
      label: $t('nav.images'),
      key: '/images',
      icon: renderIcon(ImagesOutline),
      children: imagesChildren
    },
    {
      label: $t('nav.albums'),
      key: '/albums',
      icon: renderIcon(AlbumsOutline),
      children: albumsChildren
    },
    {
      label: $t('nav.categories'),
      key: '/categories',
      icon: renderIcon(FolderOutline)
    },
    {
      label: $t('nav.favorites'),
      key: '/favorites',
      icon: renderIcon(StarOutline),
      children: [
        {
          label: $t('nav.starredImages'),
          key: '/favorites/images'
        },
        {
          label: $t('nav.starredAlbums'),
          key: '/favorites/albums'
        }
      ]
    }
  ]
})

const userMenuOptions = computed(() => [
  {
    label: $t('common.profile'),
    key: 'profile',
    icon: renderIcon(PersonOutline)
  },
  {
    label: $t('common.settings'),
    key: 'settings',
    icon: renderIcon(SettingsOutline)
  },
  {
    type: 'divider' as const,
    key: 'd1'
  },
  {
    label: $t('common.logout'),
    key: 'logout',
    icon: renderIcon(LogOutOutline)
  }
])

const languageOptions = computed(() => LOCALE_OPTIONS.map(option => ({
  label: option.label,
  key: option.value
})))

const handleMenuSelect = (key: string) => {
  // If key contains query params, parse and navigate
  if (key.includes('?')) {
    const [path, queryString] = key.split('?')
    const params = new URLSearchParams(queryString)
    const query: Record<string, string> = {}
    params.forEach((value, key) => {
      query[key] = value
    })
    router.push({ path, query })
  } else {
    router.push(key)
  }
}

const handleUserMenuSelect = (key: string) => {
  switch (key) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      userStore.logout()
      message.success($t('auth.logoutSuccess'))
      router.push('/login')
      break
  }
}

const handleLanguageSelect = (key: string) => {
  appStore.setLocale(key)
}

const handleUpload = async (options: UploadCustomRequestOptions) => {
  try {
    const file = options.file.file as File
    await imageStore.uploadImage(file, uploadTitle.value)
    message.success($t('image.uploadSuccess'))
    showUploadModal.value = false
    uploadTitle.value = ''
    options.onFinish()
    // Refresh images if on images page
    if (route.path === '/images') {
      imageStore.fetchImages()
    }
  } catch (error: any) {
    message.error(error.message || $t('image.uploadFailed'))
    options.onError()
  }
}
</script>

<style scoped>
.app-layout {
  height: 100vh;
}

.header {
  padding: 0 24px;
  display: flex;
  align-items: center;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.logo {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.content {
  padding: 24px;
  overflow: auto;
}
</style>
