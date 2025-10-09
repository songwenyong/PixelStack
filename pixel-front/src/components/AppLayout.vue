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
              Upload
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

            <n-button
              v-else
              @click="router.push('/login')"
            >
              Login
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
      title="Upload Image"
      style="width: 600px"
    >
      <n-upload
        :custom-request="handleUpload"
        :max="1"
        accept="image/*"
        list-type="image-card"
      >
        <n-button>Select Image</n-button>
      </n-upload>

      <n-form-item label="Title (Optional)" style="margin-top: 16px">
        <n-input
          v-model:value="uploadTitle"
          placeholder="Enter image title"
        />
      </n-form-item>
    </n-modal>
  </n-layout>
</template>

<script setup lang="ts">
import { ref, computed, h } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMessage, NIcon } from 'naive-ui'
import {
  HomeOutline,
  ImagesOutline,
  AlbumsOutline,
  FolderOutline,
  StarOutline,
  CloudUploadOutline,
  PersonOutline,
  LogOutOutline,
  SettingsOutline
} from '@vicons/ionicons5'
import { useUserStore } from '@/stores/user'
import { useImageStore } from '@/stores/image'
import type { UploadCustomRequestOptions } from 'naive-ui'

const router = useRouter()
const route = useRoute()
const message = useMessage()
const userStore = useUserStore()
const imageStore = useImageStore()

const collapsed = ref(false)
const showUploadModal = ref(false)
const uploadTitle = ref('')

const activeKey = computed(() => route.path)

const renderIcon = (icon: any) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions = computed(() => [
  {
    label: 'Home',
    key: '/',
    icon: renderIcon(HomeOutline)
  },
  {
    label: 'Images',
    key: '/images',
    icon: renderIcon(ImagesOutline)
  },
  {
    label: 'Albums',
    key: '/albums',
    icon: renderIcon(AlbumsOutline)
  },
  {
    label: 'Categories',
    key: '/categories',
    icon: renderIcon(FolderOutline)
  },
  {
    label: 'Favorites',
    key: '/favorites',
    icon: renderIcon(StarOutline),
    children: [
      {
        label: 'Starred Images',
        key: '/favorites/images'
      },
      {
        label: 'Starred Albums',
        key: '/favorites/albums'
      }
    ]
  }
])

const userMenuOptions = computed(() => [
  {
    label: 'Profile',
    key: 'profile',
    icon: renderIcon(PersonOutline)
  },
  {
    label: 'Settings',
    key: 'settings',
    icon: renderIcon(SettingsOutline)
  },
  {
    type: 'divider' as const,
    key: 'd1'
  },
  {
    label: 'Logout',
    key: 'logout',
    icon: renderIcon(LogOutOutline)
  }
])

const handleMenuSelect = (key: string) => {
  router.push(key)
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
      message.success('Logged out successfully')
      router.push('/login')
      break
  }
}

const handleUpload = async (options: UploadCustomRequestOptions) => {
  try {
    const file = options.file.file as File
    await imageStore.uploadImage(file, uploadTitle.value)
    message.success('Image uploaded successfully!')
    showUploadModal.value = false
    uploadTitle.value = ''
    options.onFinish()
    // Refresh images if on images page
    if (route.path === '/images') {
      imageStore.fetchImages()
    }
  } catch (error: any) {
    message.error(error.message || 'Upload failed')
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
